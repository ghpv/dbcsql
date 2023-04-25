package ee.taltech.dbcsql.core.model.sql;

public interface Statement
{
	public <T> T accept(StatementVisitor<T> visitor);
}
