package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatementBuilder;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatementBuilderBase;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class UpdatedRestrictionTranslator extends RestrictionTranslator implements RestrictionVisitor<Void>
{
	private UpdateStatementBuilderBase<UpdateStatementBuilder>.WhereClauseBuilder where;
	private UpdateStatementBuilderBase<UpdateStatementBuilder>.RelatedTablesBuilder relatedTables;
	private UpdateStatementBuilder usb;

	public UpdatedRestrictionTranslator(GenerationContext ctx, VariableDef target, UpdateStatementBuilder usb)
	{
		super(ctx, target);
		this.usb = usb;
		this.where = usb.where();
		this.relatedTables = usb.relatedTables();
	}

	@Override
	public Void visit(ConnectionRestriction r)
	{
		this.usedTablesInConnection(r,
			var ->
			{
				relatedTables.add(CommonTranslation.translateVariableForDeclaration(var));
			}
		);

		this.handleLinkWith(
			r,
			(targetColumn, otherColumn) -> usb.addValue(targetColumn, otherColumn)
		);
		return null;
	}

	public boolean isTargetColumn(ColumnDef col)
	{
		return this
			.target
			.getTable()
			.getAliasedName()
			.equals(
				col
				.getTable()
				.getAliasedName()
			)
		;
	}

	public boolean isTarget(VariableDef var)
	{
		return this
			.target
			.getAlias()
			.equals(
				var
				.getAlias()
			)
		;
	}

	@Override
	public Void visit(ExpressionRestriction r)
	{
		this.handleExpression(
			r,
			x -> relatedTables.add(x),
			x -> relatedTables.remove(x),
			x -> where.extendWith(x)
		);
		return null;
	}

	public void done()
	{
		if (this.where != null) where.build();
		if (this.relatedTables != null) relatedTables.build();
	}
}
