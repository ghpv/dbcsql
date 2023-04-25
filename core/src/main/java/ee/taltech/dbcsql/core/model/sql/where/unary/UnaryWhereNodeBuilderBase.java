package ee.taltech.dbcsql.core.model.sql.where.unary;

import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNodeBuilderBase;

public class UnaryWhereNodeBuilderBase<BuilderT extends UnaryWhereNodeBuilderBase<BuilderT>>
{
	protected UnaryWhereNode data = new UnaryWhereNode();

	@SuppressWarnings("unchecked")
	public BuilderT withNode(WhereNode node)
	{
		this.data.setNode(node);
		return (BuilderT) this;
	}

	public class ComparisonWhereNodeSubBuilder extends ComparisonWhereNodeBuilderBase<ComparisonWhereNodeSubBuilder>
	{
		private BuilderT owner;

		protected ComparisonWhereNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withNode(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public ComparisonWhereNodeSubBuilder makeComparison()
	{
		return new ComparisonWhereNodeSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNegate(boolean negate)
	{
		this.data.setNegate(negate);
		return (BuilderT) this;
	}

	public BuilderT withNegate()
	{
		return this.withNegate(true);
	}
}
