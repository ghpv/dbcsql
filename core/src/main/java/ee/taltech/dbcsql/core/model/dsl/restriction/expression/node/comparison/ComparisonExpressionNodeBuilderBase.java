package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.db.AliasedName;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ComparisonExpressionNodeBuilderBase<BuilderT extends ComparisonExpressionNodeBuilderBase<BuilderT>>
{
	protected ComparisonExpressionNode data = new ComparisonExpressionNode();

	public BuilderT withColumn(VariableDef owner, String column)
	{
		return this.withColumn(owner, new AliasedName(column));
	}
	@SuppressWarnings("unchecked")
	public BuilderT withColumn(VariableDef owner, AliasedName column)
	{
		this.data.setColumn(owner, column);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withType(ComparisonType var)
	{
		this.data.setType(var);
		return (BuilderT) this;
	}

	@SuppressWarnings("unchecked")
	public BuilderT withTargets(List<ComparisonTargetNode> var)
	{
		this.data.setTargets(var);
		return (BuilderT) this;
	}

	public BuilderT withTargets(ComparisonTargetNode... var)
	{
		return this.withTargets(List.of(var));
	}

	public BuilderT withLiteralTargets(String... var)
	{
		List<ComparisonTargetNode> nodes = Arrays
			.asList(var)
			.stream()
			.map(x -> new LiteralComparisonTargetNode(x))
			.collect(Collectors.toList())
		;
		return this.withTargets(nodes);
	}
}
