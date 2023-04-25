package ee.taltech.dbcsql.core.model.sql.where;

import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNodeBuilderBase;

public class WhereClauseBuilderBase<BuilderT extends WhereClauseBuilderBase<BuilderT>>
{
	protected WhereClause data = new WhereClause();

	public class CompositeWhereNodeSubBuilder extends CompositeWhereNodeBuilderBase<CompositeWhereNodeSubBuilder>
	{
		private BuilderT owner;

		protected CompositeWhereNodeSubBuilder(BuilderT owner)
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
	public CompositeWhereNodeSubBuilder makeNode()
	{
		return new CompositeWhereNodeSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withNode(WhereNode node)
	{
		this.data.setNode(node);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT extendWith(WhereNode node)
	{
		this.data.combineWith(node);
		return (BuilderT) this;
	}
}
