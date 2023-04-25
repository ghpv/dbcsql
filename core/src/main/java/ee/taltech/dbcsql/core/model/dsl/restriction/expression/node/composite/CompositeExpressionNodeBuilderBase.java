package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNodeBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNodeBuilderBase;

public class CompositeExpressionNodeBuilderBase<BuilderT extends CompositeExpressionNodeBuilderBase<BuilderT>>
{
	protected CompositeExpressionNode data = new CompositeExpressionNode();

	@SuppressWarnings("unchecked")
	public BuilderT withOperation(BooleanOperation op)
	{
		this.data.setOperation(op);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNodes(List<ExpressionNode> nodes)
	{
		this.data.setNodes(nodes);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addNode(ExpressionNode node)
	{
		this.data.addNode(node);
		return (BuilderT) this;
	}

	public class CompositeExpressionNodeSubBuilder extends CompositeExpressionNodeBuilderBase<CompositeExpressionNodeSubBuilder>
	{
		private BuilderT owner;

		public CompositeExpressionNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.addNode(this.data);
			return this.owner;
		}
	};

	@SuppressWarnings("unchecked")
	public CompositeExpressionNodeSubBuilder extendWithComposite()
	{
		return new CompositeExpressionNodeSubBuilder((BuilderT) this);
	}

	public class ComparisonExpressionNodeSubBuilder extends ComparisonExpressionNodeBuilderBase<ComparisonExpressionNodeSubBuilder>
	{
		private BuilderT owner;

		public ComparisonExpressionNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.addNode(this.data);
			return this.owner;
		}
	};

	@SuppressWarnings("unchecked")
	public ComparisonExpressionNodeSubBuilder extendWithComparison()
	{
		return new ComparisonExpressionNodeSubBuilder((BuilderT) this);
	}

	public class UnaryExpressionNodeSubBuilder extends UnaryExpressionNodeBuilderBase<UnaryExpressionNodeSubBuilder>
	{
		private BuilderT owner;

		public UnaryExpressionNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.addNode(this.data);
			return this.owner;
		}
	};

	@SuppressWarnings("unchecked")
	public UnaryExpressionNodeSubBuilder extendWithUnary()
	{
		return new UnaryExpressionNodeSubBuilder((BuilderT) this);
	}
}
