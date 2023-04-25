package ee.taltech.dbcsql.process.postgres.translate;

import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionVisitor;
import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPrecondition;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPrecondition;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationContext;

public class PrecondtionToRestrictionVisitor extends ConditionToRestrictionTranslator implements PreconditionVisitor<Void>
{
	public PrecondtionToRestrictionVisitor(GenerationContext ctx)
	{
		super(ctx);
	}

	@Override
	public Void visit(ExistsPrecondition c)
	{
		if (!isIgnored(c.getTarget()))
		{
			this.reg.extendRestriction(c.getRestrictions());
		}
		return null;
	}

	@Override
	public Void visit(ConnectionPrecondition c)
	{
		VariableDef a = c.getA();
		VariableDef b = c.getB();

		boolean newValue = false;
		boolean unlink = false;

		if (!isIgnored(a) && !isIgnored(b))
		{
			this.linkVariables(a, b, c.getNames(), newValue, unlink);
		}

		return null;
	}
}
