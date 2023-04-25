package ee.taltech.dbcsql.core.model.sql.where;

public interface WhereNode
{
	public <T> T accept(WhereNodeVisitor<T> visitor);
}
