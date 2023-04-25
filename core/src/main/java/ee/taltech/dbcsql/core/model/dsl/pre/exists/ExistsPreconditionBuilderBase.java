package ee.taltech.dbcsql.core.model.dsl.pre.exists;

import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDefBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ExistsPreconditionBuilderBase<BuilderT extends ExistsPreconditionBuilderBase<BuilderT>>
{
	protected ExistsPrecondition data = new ExistsPrecondition();

	@SuppressWarnings("unchecked")
	public BuilderT withTarget(VariableDef target)
	{
		this.data.setTarget(target);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withRestriction(RestrictionDef restriction)
	{
		this.data.setRestrictions(restriction);
		return (BuilderT) this;
	}

	public class RestrictionDefSubBuilder extends RestrictionDefBuilderBase<RestrictionDefSubBuilder>
	{
		BuilderT owner;
		public RestrictionDefSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withRestriction(this.data);
			return this.owner;
		}
	};

	@SuppressWarnings("unchecked")
	public RestrictionDefSubBuilder makeRestriction()
	{
		return new RestrictionDefSubBuilder((BuilderT) this);
	}
}
