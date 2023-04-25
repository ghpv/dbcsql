package ee.taltech.dbcsql.core.model.dsl.restriction;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.ExpressionRestrictionBuilderBase;

public class RestrictionDefBuilderBase<BuilderT extends RestrictionDefBuilderBase<BuilderT>>
{
	protected RestrictionDef data = new RestrictionDef();

	@SuppressWarnings("unchecked")
	public BuilderT withRestrictions(List<Restriction> restrictions)
	{
		this.data.extendWith(restrictions);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withRestriction(Restriction restriction)
	{
		this.data.extendWith(restriction);
		return (BuilderT) this;
	}

	public BuilderT withRestrictions(Restriction... restrictions)
	{
		return this.withRestrictions(List.of(restrictions));
	}

	public class ExpressionRestrictionSubBuilder extends ExpressionRestrictionBuilderBase<ExpressionRestrictionSubBuilder>
	{
		BuilderT owner;
		protected ExpressionRestrictionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.data.extendWith(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public ExpressionRestrictionSubBuilder makeExpressionRestriction()
	{
		return new ExpressionRestrictionSubBuilder((BuilderT) this);
	}
}
