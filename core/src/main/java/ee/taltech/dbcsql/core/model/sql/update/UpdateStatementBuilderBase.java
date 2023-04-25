package ee.taltech.dbcsql.core.model.sql.update;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.dsl.post.ReturnValue;
import ee.taltech.dbcsql.core.model.sql.uvalues.UniqueValuesBuilderBase;
import ee.taltech.dbcsql.core.model.sql.where.WhereClauseBuilderBase;

public class UpdateStatementBuilderBase <BuilderT extends UpdateStatementBuilderBase<BuilderT>>
{
	protected UpdateStatement data = new UpdateStatement();

	@SuppressWarnings("unchecked")
	public BuilderT into(String table)
	{
		this.data.setTable(table);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addValue(String column, String value)
	{
		this.data.addValue(column, value);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT addValue(ColumnDef column, String value)
	{
		this.data.addValue(column, value);
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

	public class RelatedTablesBuilder extends UniqueValuesBuilderBase<RelatedTablesBuilder>
	{
		private BuilderT parent;

		public RelatedTablesBuilder(BuilderT parent)
		{
			this.parent = parent;
		}

		public BuilderT build()
		{
			if (!this.data.getValues().isEmpty())
			{
				this.parent.data.setRelatedTables(this.data);
			}
			return this.parent;
		}
	};

	@SuppressWarnings("unchecked")
	public RelatedTablesBuilder relatedTables()
	{
		return new RelatedTablesBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withReturnValue(ReturnValue val)
	{
		this.data.setReturnValue(val);
		return (BuilderT) this;
	}
}
