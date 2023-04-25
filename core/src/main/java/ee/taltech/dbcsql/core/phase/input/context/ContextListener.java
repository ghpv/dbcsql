package ee.taltech.dbcsql.core.phase.input.context;

import ee.taltech.dbcsql.core.model.db.DBName;
import ee.taltech.dbcsql.core.model.db.DSLName;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder.FKeySubBuilder;
import ee.taltech.dbcsql.core.model.db.DatabaseDefBuilder.TableDefSubBuilder;
import ee.taltech.dbcsql.core.model.db.FKey;
import ee.taltech.dbcsql.core.phase.input.ContextBaseListener;
import ee.taltech.dbcsql.core.phase.input.ContextParser.ColumnDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContextParser.ConnectedColumnsDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContextParser.ConnectionDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContextParser.DbNameContext;
import ee.taltech.dbcsql.core.phase.input.ContextParser.IdentifierDeclContext;
import ee.taltech.dbcsql.core.phase.input.ContextParser.TableDeclContext;
import ee.taltech.dbcsql.core.phase.input.DSLListener;

public class ContextListener extends ContextBaseListener implements DSLListener<DatabaseDef>
{
	private DatabaseDefBuilder database = new DatabaseDefBuilder();
	private TableDefSubBuilder table;
	private FKeySubBuilder connection;

	public ContextListener()
	{
	}

	@Override
	public void enterTableDecl(TableDeclContext ctx)
	{
		String dslNameStr = ctx.dsl_name.getText();
		DSLName dslName = new DSLName(dslNameStr);
		DBName dbName = new DBName(dslNameStr);
		if (ctx.db_name != null)
		{
			dbName = this.parseDBName(ctx.db_name);
		}

		if (ctx.parent_table != null)
		{
			this.table = this
				.database
				.makeExtendingTable(this.parseDBName(ctx.parent_table))
			;
		}
		else
		{
			this.table = this
				.database
				.makeTable()
			;
		}

		this.table.withName(dslName, dbName);
	}

	@Override
	public void enterColumnDecl(ColumnDeclContext ctx)
	{
		assert this.table != null: "Column decl must be within a table";
		String dslName = ctx.dsl_name.getText();
		String dbName = dslName;
		if (ctx.db_name != null)
		{
			dbName = ctx.db_name.getText();
		}
		this.table.withColumn(dslName, dbName, ctx.type.getText());
	}

	@Override
	public void exitTableDecl(TableDeclContext ctx)
	{
		this.table.build();
		this.table = null;
	}

	@Override
	public void enterConnectionDecl(ConnectionDeclContext ctx)
	{
		String name = FKey.DEFAULT_NAME;
		if (ctx.name != null)
		{
			name = ctx.name.getText();
		}
		DBName l = this.parseDBName(ctx.left);
		DBName r = this.parseDBName(ctx.right);
		this.connection = this
			.database
			.makeConnection()
			.betweenTables(l, r)
			.withName(name);
		;
	}

	private DBName parseDBName(DbNameContext ctx)
	{
		String name = ctx.db_name.getText();
		String schema = null;

		if (ctx.schema != null)
		{
			schema = ctx.schema.getText();
		}
		return new DBName(schema, name);
	}

	@Override
	public void enterIdentifierDecl(IdentifierDeclContext ctx)
	{
		this.database.withIdentifier(this.parseDBName(ctx.table), ctx.column.getText());
	}

	@Override
	public void enterConnectedColumnsDecl(ConnectedColumnsDeclContext ctx)
	{
		assert this.connection != null: "Column connection decl must be within a connection declaration";
		this.connection.connectColumns(ctx.left.getText(), ctx.right.getText());
	}

	@Override
	public void exitConnectionDecl(ConnectionDeclContext ctx)
	{
		this.connection.build();
		this.connection = null;
	}

	@Override
	public DatabaseDef getData()
	{
		return this.database.build();
	}
}
