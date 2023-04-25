package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;

public class ReturnIdentifierVisitor implements PostconditionVisitor<Void>
{
	private static final String IDENTIFIER = "_identifier";

	@Override
	public Void visit(DeletedPostcondition c)
	{
		c.setReturnValue(getRV());
		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		c.setReturnValue(getRV());
		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		c.setReturnValue(getRV());
		return null;
	}

	private ReturnValue getRV()
	{
		return new ReturnValue(IDENTIFIER);
	}
}
