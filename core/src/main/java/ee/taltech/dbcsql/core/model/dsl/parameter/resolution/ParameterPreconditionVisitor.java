package ee.taltech.dbcsql.core.model.dsl.parameter.resolution;

import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPrecondition;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPrecondition;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;

public class ParameterPreconditionVisitor implements PreconditionVisitor<Void>
{
	private ParameterResolver resolver;

	public ParameterPreconditionVisitor(ParameterResolver resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public Void visit(ExistsPrecondition c)
	{
		ParameterRestrictionVisitor visitor = new ParameterRestrictionVisitor(this.resolver);
		for (Restriction r: c.getRestrictions().getRestrictions())
		{
			r.accept(visitor);
		}
		return null;
	}

	@Override
	public Void visit(ConnectionPrecondition c)
	{
		return null;
	}
}
