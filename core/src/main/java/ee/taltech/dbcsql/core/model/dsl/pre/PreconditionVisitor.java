package ee.taltech.dbcsql.core.model.dsl.pre;

import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPrecondition;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPrecondition;

public interface PreconditionVisitor <T>
{
	public T visit(ExistsPrecondition c);
	public T visit(ConnectionPrecondition c);
}
