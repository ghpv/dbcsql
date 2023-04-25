package ee.taltech.dbcsql.core.model.dsl.post;

public interface PostconditionDef
{
	public <T> T accept(PostconditionVisitor<T> visitor);
}
