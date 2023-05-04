package ee.taltech.dbcsql.core.model.dsl.parameter.resolution;

import java.util.Collection;

import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostcondition;

public class ParameterPostconditionVisitor implements PostconditionVisitor<Void>
{
	private ParameterResolver resolver;

	public ParameterPostconditionVisitor(ParameterResolver resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public Void visit(DeletedPostcondition c)
	{
		return null;
	}

	@Override
	public Void visit(InsertedPostcondition c)
	{
		this
			.resolveFromColumnEquality(c.getValues())
		;
		return null;
	}

	@Override
	public Void visit(UpdatedPostcondition c)
	{
		this
			.resolveFromColumnEquality(c.getValues())
		;
		return null;
	}

	private ParameterPostconditionVisitor resolveFromColumnEquality(Collection<ColumnEquality> equalColumns)
	{
		for (ColumnEquality eq: equalColumns)
		{
			this.resolver.consider(eq);
		}
		return this;
	}
}
