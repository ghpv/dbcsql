package ee.taltech.dbcsql.process.postgres.translate;

import java.util.List;
import java.util.function.Consumer;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.VariableLink;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPrecondition;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPrecondition;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class SearchPathResolverRestrictionVisitor implements PreconditionVisitor<Void>, PostconditionVisitor<Void>
{
	private Consumer<String> callback;
	private DatabaseDef db;

	public SearchPathResolverRestrictionVisitor(GenerationContext ctx, Consumer<String> callback)
	{
		this.callback = callback;
		this.db = ctx.getDatabaseDef();
	}

	@Override
	public Void visit(ExistsPrecondition c)
	{
		this.accept(c.getTarget());
		return null;
	}

	@Override
	public Void visit(ConnectionPrecondition c)
	{
		this.accept(
			c.getA().getTable(),
			c.getB().getTable(),
			c.getNames()
		);
		return null;
	}

	@Override
	public Void visit(DeletedPostcondition c)
	{
		this.accept(c.getTarget());
		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		this
			.accept(c.getTarget())
			.accept(c.getTarget(), c.getLinks())
		;
		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		this
			.accept(c.getTarget())
			.accept(c.getTarget(), c.getLinks())
		;
		return null;
	}

	private SearchPathResolverRestrictionVisitor accept(VariableDef target, List<VariableLink> links)
	{
		for (VariableLink linked: links)
		{
			this.accept(
				target.getTable(),
				linked.getKey().getTable(),
				linked.getValue()
			);
		}
		return this;
	}
	private SearchPathResolverRestrictionVisitor accept(TableDef t1, TableDef t2, List<String> names)
	{
		for (FKey connection: this.db.getConnection(t1, t2, names))
		{
			this
				.accept(connection.getA())
				.accept(connection.getB())
			;
		}
		return this;
	}
	private SearchPathResolverRestrictionVisitor accept(VariableDef var)
	{
		return this.accept(var.getTable());
	}
	private SearchPathResolverRestrictionVisitor accept(TableDef td)
	{
		if (td.getAliasedName().getDBName().getSchema().isEmpty())
		{
			return this;
		}

		this.callback.accept(
			td
			.getAliasedName()
			.getDBName()
			.getSchema()
			.get()
		);
		return this;
	}
}
