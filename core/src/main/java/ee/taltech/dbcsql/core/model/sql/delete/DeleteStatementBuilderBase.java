package ee.taltech.dbcsql.core.model.sql.delete;

import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.sql.uvalues.UniqueValuesBuilderBase;
import ee.taltech.dbcsql.core.model.sql.where.WhereClauseBuilderBase;

public class DeleteStatementBuilderBase <BuilderT extends DeleteStatementBuilderBase<BuilderT>>
{
	protected DeleteStatement data = new DeleteStatement();

	@SuppressWarnings("unchecked")
	public BuilderT from(String table)
	{
		this.data.setTable(table);
		return (BuilderT) this;
	}

	public class WhereClauseBuilder extends WhereClauseBuilderBase<WhereClauseBuilder>
	{
		private BuilderT parent;

		public WhereClauseBuilder(BuilderT parent)
		{
			this.parent = parent;
		}

		public BuilderT build()
		{
			if (this.data.getNode() != null)
			{
				this.parent.data.setWhere(this.data);
			}
			return this.parent;
		}
	};

	@SuppressWarnings("unchecked")
	public WhereClauseBuilder where()
	{
		return new WhereClauseBuilder((BuilderT) this);
	}

	public class UsingClauseBuilder extends UniqueValuesBuilderBase<UsingClauseBuilder>
	{
		private BuilderT parent;

		public UsingClauseBuilder(BuilderT parent)
		{
			this.parent = parent;
		}

		public BuilderT build()
		{
			if (!this.data.getValues().isEmpty())
			{
				this.parent.data.setUsing(this.data);
			}
			return this.parent;
		}
	};

	@SuppressWarnings("unchecked")
	public UsingClauseBuilder using()
	{
		return new UsingClauseBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withReturnValue(ReturnValue val)
	{
		this.data.setReturnValue(val);
		return (BuilderT) this;
	}
}
