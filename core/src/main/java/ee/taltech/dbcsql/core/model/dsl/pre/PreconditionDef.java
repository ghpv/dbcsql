package ee.taltech.dbcsql.core.model.dsl.pre;

public interface PreconditionDef
{
	public <T> T accept(PreconditionVisitor<T> visitor);
}
