package ee.taltech.dbcsql.core.model.sql;

import ee.taltech.dbcsql.core.model.sql.delete.DeleteStatementBuilderBase;

public class FunctionBuilder
{
	private Function data = new Function();

	public FunctionBuilder withName(String name)
	{
		this.data.setName(name);
		return this;
	}

	public FunctionBuilder addArgument(String arg)
	{
		this.data.extendArgs(arg);
		return this;
	}

	public FunctionBuilder addStatement(Statement statement)
	{
		this.data.extendStatements(statement);
		return this;
	}

	public class DeleteStatementSubBuilder extends DeleteStatementBuilderBase<DeleteStatementSubBuilder>
	{
		private FunctionBuilder owner;

		protected DeleteStatementSubBuilder(FunctionBuilder owner)
		{
			this.owner = owner;
		}

		public FunctionBuilder build()
		{
			this.owner.addStatement(this.data);
			return this.owner;
		}
	}

	public DeleteStatementSubBuilder makeDeleteStatement()
	{
		return new DeleteStatementSubBuilder(this);
	}

	public FunctionBuilder withReturnType(String type)
	{
		this.data.setReturnType(type);
		return this;
	}

	public FunctionBuilder withSearchSpace(String schema)
	{
		this.data.extendSearchSpace(schema);
		return this;
	}

	public FunctionBuilder removeSearchSpace(String schema)
	{
		this.data.removeSearchSpace(schema);
		return this;
	}

	public FunctionBuilder withSecurityInvoker(boolean b)
	{
		this.data.setSecurityInvoker(b);
		return this;
	}

	public Function build()
	{
		return this.data;
	}
}
