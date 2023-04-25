package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.db.AliasedName;
import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.DBName;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class CommonTranslation
{
	public static String translateTableName(TableDef table)
	{
		DBName name = table.getAliasedName().getDBName();

		StringBuilder sb = new StringBuilder();
		if (name.getSchema().isPresent())
		{
			sb
				.append(name.getSchema().get())
				.append(".")
			;
		}
		return sb
			.append(name.getName())
			.toString()
		;
	}

	public static String translateColumnName(VariableDef owner, ColumnDef column)
	{
		return translateColumnName(
			owner,
			column.getAliasedName()
		);
	}
	public static String translateColumnName(VariableDef owner, AliasedName columnName)
	{
		return translateColumnName(
			owner,
			columnName.getDBName().getName()
		);
	}
	public static String translateColumnName(VariableDef owner, String column)
	{
		return new StringBuilder()
			.append(owner.getAlias())
			.append(".")
			.append(column)
			.toString()
		;
	}

	public static String translateVariableForDeclaration(VariableDef var)
	{
		StringBuilder sb = new StringBuilder();
		return sb
			.append(translateTableName(var.getTable()))
			.append(" as ")
			.append(var.getAlias())
			.toString()
		;
	}
}
