package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;
import java.util.Optional;
import java.util.function.BiConsumer;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.db.PKey;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatementBuilder;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatementBuilder;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatementBuilder;
import ee.taltech.dbcsql.core.phase.GenerationContext;
import ee.taltech.dbcsql.core.phase.TranslatorInputException;
import ee.taltech.dbcsql.core.phase.output.RestrictionRegistry;

public class PostconditionTranslator implements PostconditionVisitor<Void>
{
	private static final Object IDENTIFIER = "_identifier";
	private Statement statement;
	private RestrictionRegistry reg;
	private GenerationContext ctx;

	public PostconditionTranslator(GenerationContext ctx)
	{
		this.ctx = ctx;
		this.reg = ctx.getRestrictionRegistry();
	}

	@Override
	public Void visit(DeletedPostcondition c)
	{
		DeleteStatementBuilder dsb = new DeleteStatementBuilder()
			.from(CommonTranslation.translateVariableForDeclaration(c.getTarget()))
		;
		if (c.getReturnValue().isPresent())
		{
			preprocessReturnValue(c.getTarget(), c.getReturnValue().get());
			dsb.withReturnValue(c.getReturnValue().get());
		}
		DeletedRestrictionTranslator translator = new DeletedRestrictionTranslator(ctx, c.getTarget(), dsb);
		for (Restriction r: reg.getRestrictions().getRestrictions())
		{
			r.accept(translator);
		}
		translator.done();
		this.statement = dsb.build();

		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		InsertStatementBuilder isb = new InsertStatementBuilder()
			.into(CommonTranslation.translateVariableForDeclaration(c.getTarget()))
		;
		if (c.getReturnValue().isPresent())
		{
			preprocessReturnValue(c.getTarget(), c.getReturnValue().get());
			isb.withReturnValue(c.getReturnValue().get());
		}
		this.processColumnEquality(
			c.getTarget(),
			c.getValues(),
			(cd, val) -> isb.addValue(cd, val)
		);
		InsertedRestrictionTranslator translator = new InsertedRestrictionTranslator(ctx, c.getTarget(), isb);
		for (Restriction r: reg.getRestrictions().getRestrictions())
		{
			r.accept(translator);
		}
		translator.done();
		this.statement = isb.build();

		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		UpdateStatementBuilder usb = new UpdateStatementBuilder()
			.into(CommonTranslation.translateVariableForDeclaration(c.getTarget()))
		;
		if (c.getReturnValue().isPresent())
		{
			preprocessReturnValue(c.getTarget(), c.getReturnValue().get());
			usb.withReturnValue(c.getReturnValue().get());
		}
		this.processColumnEquality(
			c.getTarget(),
			c.getValues(),
			(cd, val) -> usb.addValue(cd, val)
		);
		UpdatedRestrictionTranslator translator = new UpdatedRestrictionTranslator(ctx, c.getTarget(), usb);
		for (Restriction r: reg.getRestrictions().getRestrictions())
		{
			r.accept(translator);
		}
		translator.done();
		this.statement = usb.build();

		return null;
	}

	private void processColumnEquality(
		VariableDef target,
		Collection<ColumnEquality> equalities,
		BiConsumer<ColumnDef, String> callback
	) {
		for (ColumnEquality ce: equalities)
		{
			this.preprocessColumnEquality(target, ce);
			ColumnDef cd = ce.getKey();
			String val = ce.getValue().accept(new ComparisonTargetNodeStringifierVisitor());
			callback.accept(cd, val);
		}
	}

	private void preprocessReturnValue(VariableDef target, ReturnValue rv)
	{
		this
			.handleReturnIdentifier(target, rv)
			.translateReturnColumnNameIfSet(target, rv)
		;
	}

	private PostconditionTranslator handleReturnIdentifier(VariableDef target, ReturnValue rv)
	{
		if (!rv.getSymbolValue().equals(IDENTIFIER))
		{
			return this;
		}
		Optional<PKey> pkeyOpt = target.getTable().getIdentifier();
		if (pkeyOpt.isEmpty())
		{
			throw new TranslatorInputException("Asked to return identifier, but '" + target.getTable().getAliasedName().getDSLName().getName() + "' does not have an identifier!");
		}
		rv.setColumn(pkeyOpt.get().getColumn());
		return this;
	}

	private void preprocessColumnEquality(VariableDef target, ColumnEquality ce)
	{
		ComparisonTargetNodePreprocessorVisitor v = new ComparisonTargetNodePreprocessorVisitor(target);
		ce.getValue().accept(v);
	}

	private PostconditionTranslator translateReturnColumnNameIfSet(VariableDef target, ReturnValue rv)
	{
		if (rv.getColumn().isEmpty())
		{
			return this;
		}
		if (!target.getTable().hasColumn(rv.getColumn().get()))
		{
			return this;
		}
		rv.setSymbolValue(CommonTranslation.translateColumnName(target, rv.getColumn().get()));
		return this;
	}

	public Statement getStatement()
	{
		return this.statement;
	}
}
