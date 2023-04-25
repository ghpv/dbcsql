package ee.taltech.dbcsql.core.model.dsl.argument.resolution;

import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPrecondition;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPrecondition;
import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;

public class ArgumentPreconditionVisitor implements PreconditionVisitor<Void>
{
	private ArgumentResolver resolver;

	public ArgumentPreconditionVisitor(ArgumentResolver resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public Void visit(ExistsPrecondition c)
	{
		ArgumentRestrictionVisitor visitor = new ArgumentRestrictionVisitor(this.resolver);
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
