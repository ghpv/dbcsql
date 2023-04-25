package ee.taltech.dbcsql.core.model.sql;

import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatement;
import ee.taltech.dbcsql.core.model.sql.insert.InsertStatement;
import ee.taltech.dbcsql.core.model.sql.update.UpdateStatement;

public interface StatementVisitor <T>
{
	public T visit(DeleteStatement v);
	public T visit(InsertStatement v);
	public T visit(UpdateStatement v);
}
