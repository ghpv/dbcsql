package ee.taltech.dbcsql.core.model.sql.where.comparison;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;

public class ComparisonWhereNodeBuilderBase<BuilderT extends ComparisonWhereNodeBuilderBase<BuilderT>>
{
	protected ComparisonWhereNode data = new ComparisonWhereNode();

	@SuppressWarnings("unchecked")
	public BuilderT withColumn(String var)
	{
		this.data.setColumn(var);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withType(ComparisonType var)
	{
		this.data.setType(var);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withTargets(List<String> var)
	{
		this.data.setTargets(var);
		return (BuilderT) this;
	}

	public BuilderT withTargets(String... var)
	{
		return this.withTargets(List.of(var));
	}
}
