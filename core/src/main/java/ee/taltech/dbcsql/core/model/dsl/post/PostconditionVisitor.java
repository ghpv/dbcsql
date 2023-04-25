package ee.taltech.dbcsql.core.model.dsl.post;

import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;

public interface PostconditionVisitor <T>
{
	public T visit(DeletedPostcondition c);
	public T visit(InsertedPostcondition c);
	public T visit(UpdatedPostcondition c);
}
