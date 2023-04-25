package ee.taltech.dbcsql.core.model.dsl.restriction.expression;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNodeBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNodeBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ExpressionRestrictionBuilderBase<BuilderT extends ExpressionRestrictionBuilderBase<BuilderT>>
{
	protected ExpressionRestriction data = new ExpressionRestriction();

	public class CompositeExpressionNodeSubBuilder extends CompositeExpressionNodeBuilderBase<CompositeExpressionNodeSubBuilder>
	{
		private BuilderT owner;

		protected CompositeExpressionNodeSubBuilder(BuilderT owner)
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
	public CompositeExpressionNodeSubBuilder makeNode()
	{
		return new CompositeExpressionNodeSubBuilder((BuilderT) this);
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
	public BuilderT withNode(ExpressionNode node)
	{
		this.data.setNode(node);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withTarget(VariableDef target)
	{
		this.data.setTarget(target);
		return (BuilderT) this;
	}
}
