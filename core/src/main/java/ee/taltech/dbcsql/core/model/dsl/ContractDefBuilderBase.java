package ee.taltech.dbcsql.core.model.dsl;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.parameter.ParameterDef;
import ee.taltech.dbcsql.core.model.dsl.parameter.ParameterDefBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.post.PostconditionDef;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostconditionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.post.inserted.InsertedPostconditionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.post.updated.UpdatedPostconditionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.pre.PreconditionDef;
import ee.taltech.dbcsql.core.model.dsl.pre.connection.ConnectionPreconditionBuilderBase;
import ee.taltech.dbcsql.core.model.dsl.pre.exists.ExistsPreconditionBuilderBase;

public class ContractDefBuilderBase<BuilderT extends ContractDefBuilderBase<BuilderT>>
{
	protected ContractDef data = new ContractDef();

	@SuppressWarnings("unchecked")
	public BuilderT withName(String name)
	{
		this.data.setName(name);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withParameters(List<ParameterDef> parameters)
	{
		this.data.setParameters(parameters);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withParameter(ParameterDef parameter)
	{
		this.data.addParameter(parameter);
		return (BuilderT) this;
	}

	public class ParameterDefSubBuilder extends ParameterDefBuilderBase<ParameterDefSubBuilder>
	{
		BuilderT owner;
		public ParameterDefSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withParameter(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public ParameterDefSubBuilder makeParameter()
	{
		return new ParameterDefSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withPreconditions(List<PreconditionDef> conditions)
	{
		this.data.setPreconditions(conditions);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withPrecondition(PreconditionDef condition)
	{
		this.data.addPrecondition(condition);
		return (BuilderT) this;
	}

	public class ExistsPreconditionSubBuilder extends ExistsPreconditionBuilderBase<ExistsPreconditionSubBuilder>
	{
		BuilderT owner;
		public ExistsPreconditionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withPrecondition(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public ExistsPreconditionSubBuilder makeExistsPrecondition()
	{
		return new ExistsPreconditionSubBuilder((BuilderT) this);
	}

	public class ConnectionPreconditionSubBuilder extends ConnectionPreconditionBuilderBase<ConnectionPreconditionSubBuilder>
	{
		BuilderT owner;
		public ConnectionPreconditionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withPrecondition(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public ConnectionPreconditionSubBuilder makeConnectionPrecondition()
	{
		return new ConnectionPreconditionSubBuilder((BuilderT) this);
	}

	@SuppressWarnings("unchecked")
	public BuilderT withPostconditions(List<PostconditionDef> conditions)
	{
		this.data.setPostconditions(conditions);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withPostcondition(PostconditionDef condition)
	{
		this.data.addPostcondition(condition);
		return (BuilderT) this;
	}

	public class DeletedPostconditionSubBuilder extends DeletedPostconditionBuilderBase<DeletedPostconditionSubBuilder>
	{
		BuilderT owner;
		public DeletedPostconditionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withPostcondition(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public DeletedPostconditionSubBuilder makeDeletedPostcondition()
	{
		return new DeletedPostconditionSubBuilder((BuilderT) this);
	}

	public class InsertedPostconditionSubBuilder extends InsertedPostconditionBuilderBase<InsertedPostconditionSubBuilder>
	{
		BuilderT owner;
		public InsertedPostconditionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withPostcondition(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public InsertedPostconditionSubBuilder makeInsertedPostcondition()
	{
		return new InsertedPostconditionSubBuilder((BuilderT) this);
	}

	public class UpdatedPostconditionSubBuilder extends UpdatedPostconditionBuilderBase<UpdatedPostconditionSubBuilder>
	{
		BuilderT owner;
		public UpdatedPostconditionSubBuilder(BuilderT owner)
		{
			this.owner = owner;
		}

		public BuilderT build()
		{
			this.owner.withPostcondition(this.data);
			return this.owner;
		}
	}

	@SuppressWarnings("unchecked")
	public BuilderT withComment(String comm)
	{
		this.data.setComment(comm);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public UpdatedPostconditionSubBuilder makeUpdatedPostcondition()
	{
		return new UpdatedPostconditionSubBuilder((BuilderT) this);
	}
}
