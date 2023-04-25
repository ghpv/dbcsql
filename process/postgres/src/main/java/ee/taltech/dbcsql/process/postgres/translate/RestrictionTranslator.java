package ee.taltech.dbcsql.process.postgres.translate;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ConnectedColumns;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.connection.ConnectionRestriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestriction;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class RestrictionTranslator
{
	protected VariableDef target;
	protected DatabaseDef db;

	public RestrictionTranslator(GenerationContext ctx, VariableDef target)
	{
		this.db = ctx.getDatabaseDef();
		this.target = target;
	}

	protected void usedTablesInConnection(ConnectionRestriction r, Consumer<VariableDef> callback)
	{
		for (FKey fk: this.db.getConnection(r.getA().getTable(), r.getB().getTable(), r.getNames()))
		{
			VariableDef a = getVarFor(r, fk.getA());
			VariableDef b = getVarFor(r, fk.getB());
			this
				.usingVariable(a, callback)
				.usingVariable(b, callback)
			;
		}
	}

	private VariableDef getVarFor(ConnectionRestriction r, TableDef a)
	{
		if (isVarForTable(r.getA(), a))
		{
			return r.getA();
		}
		if (isVarForTable(r.getB(), a))
		{
			return r.getB();
		}
		return new VariableDef(
			a.getAliasedName().getDBName().getName(),
			a
		);
	}

	private boolean isVarForTable(VariableDef b, TableDef a)
	{
		return b.getTable().getAliasedName().equals(a.getAliasedName());
	}

	private RestrictionTranslator usingVariable(VariableDef var, Consumer<VariableDef> callback)
	{
		if (var.getAlias().equals(this.target.getAlias()))
		{
			return this;
		}
		callback.accept(var);
		return this;
	}

	protected WhereNode translateExpression(ExpressionRestriction r)
	{
		return r.getNode().accept(new RestrictionExpressionTranslator());
	}

	protected void handleLinkWith(ConnectionRestriction r, BiConsumer<String, String> callback)
	{
		VariableDef targetOtherConnection = r.getOther(this.target);
		if (r.isNewLink())
		{
			FKey connectionToTarget = this.db.getConnection(
				r.getA().getTable(),
				r.getB().getTable(),
				r.getNames()
			).get(0);

			TableDef targetTable = this.target.getTable();

			for (ConnectedColumns cols: connectionToTarget.getConnections())
			{
				ColumnDef target = cols.getOneBelongingTo(targetTable);
				ColumnDef other = cols.getOneNotBelongingTo(targetTable);

				String targetColumn = target.getAliasedName().getDBName().getName();

				String otherColumn = "null";
				if (r.isUnlink())
				{
					callback.accept(targetColumn, otherColumn);
					continue;
				}

				VariableDef otherOwner = this.makeDummyVariable(other);

				if (
					targetOtherConnection != null
					&& targetOtherConnection.getTable().getAliasedName().equals(other.getTable().getAliasedName()))
				{
					otherOwner = targetOtherConnection;
				}
				otherColumn = CommonTranslation.translateColumnName(otherOwner, other);
				callback.accept(targetColumn, otherColumn);
			}
		}
	}

	protected void handleExpression(
		ExpressionRestriction r,
		Consumer<String> addRelatedCallback,
		Consumer<String> removeRelatedCallback,
		Consumer<WhereNode> extendWhereNodeCallback
	) {
		r.getNode().accept(
			new PostgresTableDiscoveryVistor(
				var -> addRelatedCallback.accept(CommonTranslation.translateVariableForDeclaration(var))
			)
		);
		removeRelatedCallback.accept(CommonTranslation.translateVariableForDeclaration(this.target));
		extendWhereNodeCallback.accept(this.translateExpression(r));
	}

	private VariableDef makeDummyVariable(ColumnDef col)
	{
		TableDef table = col.getTable();
		String tableName = table.getAliasedName().getDBName().getName();
		return new VariableDef(tableName, table);
	}
}
