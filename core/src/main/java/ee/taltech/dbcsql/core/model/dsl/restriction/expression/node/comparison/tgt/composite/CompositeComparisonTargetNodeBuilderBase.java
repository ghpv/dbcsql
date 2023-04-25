package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNodeBuilderBase;

public class CompositeComparisonTargetNodeBuilderBase<BuilderT extends CompositeComparisonTargetNodeBuilderBase<BuilderT>>
{
	protected CompositeComparisonTargetNode data = new CompositeComparisonTargetNode();

	public class CompositeComparisonTargetNodeSubBuilder extends CompositeComparisonTargetNodeBuilderBase<CompositeComparisonTargetNodeSubBuilder>
	{
		private BuilderT owner;

		protected CompositeComparisonTargetNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.addNode(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public CompositeComparisonTargetNodeSubBuilder addComposite()
	{
		return new CompositeComparisonTargetNodeSubBuilder((BuilderT) this);
	}

	public class LiteralComparisonTargetNodeSubBuilder extends LiteralComparisonTargetNodeBuilderBase<LiteralComparisonTargetNodeSubBuilder>
	{
		private BuilderT owner;

		protected LiteralComparisonTargetNodeSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.addNode(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public LiteralComparisonTargetNodeSubBuilder addLiteral()
	{
		return new LiteralComparisonTargetNodeSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withOperator(ComparisonTargetOperation op)
	{
		this.data.setOperation(op);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addNode(ComparisonTargetNode node)
	{
		this.data.addNode(node);
		return (BuilderT) this;
	}
}
