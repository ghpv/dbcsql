package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNodeBuilderBase;

public class UnaryExpressionNodeBuilderBase<BuilderT extends UnaryExpressionNodeBuilderBase<BuilderT>>
{
	protected UnaryExpressionNode data = new UnaryExpressionNode();

	@SuppressWarnings("unchecked")
	public BuilderT withNode(ExpressionNode node)
	{
		this.data.setNode(node);
		return (BuilderT) this;
	}

	public class ComparisonExpressionNodeSubBuilder extends ComparisonExpressionNodeBuilderBase<ComparisonExpressionNodeSubBuilder>
	{
		private BuilderT owner;

		protected ComparisonExpressionNodeSubBuilder(BuilderT owner)
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
	public ComparisonExpressionNodeSubBuilder makeComparison()
	{
		return new ComparisonExpressionNodeSubBuilder((BuilderT) this);
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
