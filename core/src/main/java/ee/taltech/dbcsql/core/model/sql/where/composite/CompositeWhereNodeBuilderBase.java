package ee.taltech.dbcsql.core.model.sql.where.composite;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNodeBuilderBase;
import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNodeBuilderBase;

public class CompositeWhereNodeBuilderBase<BuilderT extends CompositeWhereNodeBuilderBase<BuilderT>>
{
	protected CompositeWhereNode data = new CompositeWhereNode();

	@SuppressWarnings("unchecked")
	public BuilderT withOperation(BooleanOperation op)
	{
		this.data.setOperation(op);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNodes(List<WhereNode> nodes)
	{
		this.data.setNodes(nodes);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addNode(WhereNode node)
	{
		this.data.addNode(node);
		return (BuilderT) this;
	}

	public class CompositeWhereNodeSubBuilder extends CompositeWhereNodeBuilderBase<CompositeWhereNodeSubBuilder>
	{
		private BuilderT owner;

		public CompositeWhereNodeSubBuilder(BuilderT owner)
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
	public CompositeWhereNodeSubBuilder extendWithComposite()
	{
		return new CompositeWhereNodeSubBuilder((BuilderT) this);
	}

	public class ComparisonWhereNodeSubBuilder extends ComparisonWhereNodeBuilderBase<ComparisonWhereNodeSubBuilder>
	{
		private BuilderT owner;

		public ComparisonWhereNodeSubBuilder(BuilderT owner)
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
	public ComparisonWhereNodeSubBuilder extendWithComparison()
	{
		return new ComparisonWhereNodeSubBuilder((BuilderT) this);
	}

	public class UnaryWhereNodeSubBuilder extends UnaryWhereNodeBuilderBase<UnaryWhereNodeSubBuilder>
	{
		private BuilderT owner;

		public UnaryWhereNodeSubBuilder(BuilderT owner)
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
	public UnaryWhereNodeSubBuilder extendWithUnary()
	{
		return new UnaryWhereNodeSubBuilder((BuilderT) this);
	}
}
