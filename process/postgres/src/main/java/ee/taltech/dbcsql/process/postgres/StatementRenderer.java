package ee.taltech.dbcsql.process.postgres;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.model.sql.StatementVisitor;
import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatement;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatement;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatement;

public class StatementRenderer implements AttributeRenderer<Statement>, StatementVisitor<String>
{
	@Override
	public String toString(Statement stmt, String arg1, Locale arg2)
	{
		return stmt.accept(this);
	}

	@Override
	public String visit(DeleteStatement v)
	{
		return PostgresTemplates
			.instance()
			.getDeletedStatement()
			.add("d", v)
			.render();
	}

	@Override
	public String visit(InsertStatement v)
	{
		return PostgresTemplates
			.instance()
			.getInsertedStatement()
			.add("d", v)
			.render();
	}

	@Override
	public String visit(UpdateStatement v) {
		return PostgresTemplates
			.instance()
			.getUpdatedStatement()
			.add("d", v)
			.render();
	}
}
