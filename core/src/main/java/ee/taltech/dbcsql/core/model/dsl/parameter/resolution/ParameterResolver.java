package ee.taltech.dbcsql.core.model.dsl.parameter.resolution;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.dsl.parameter.ParameterDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;

public class ParameterResolver
{
	public Map<String, ParameterDef> paramRef = new HashMap<>();

	public void addParameterReference(ParameterDef param)
	{
		this.paramRef.put(param.getAlias(), param);
	}

	public ParameterResolver consider(ColumnEquality eq)
	{
		return this.consider(eq.getKey().getType(), eq.getValue());
	}
	public ParameterResolver consider(ComparisonExpressionNode expr)
	{
		return this.consider(
			expr.getColumnOwner().getTable().getColumn(expr.getColumn().getDSLName()),
			expr.getTargets()
		);
	}
	public ParameterResolver consider(ColumnDef targettedColumn, Collection<ComparisonTargetNode> values)
	{
		String possibleType = targettedColumn.getType();
		for (ComparisonTargetNode s: values)
		{
			this.consider(possibleType, s);
		}
		return this;
	}
	public ParameterResolver consider(String possibleType, ComparisonTargetNode compTarget)
	{
		ParameterComparisonTargetVisitor targetVisitor = new ParameterComparisonTargetVisitor(possibleType, this);
		compTarget.accept(targetVisitor);
		return this;
	}

	public ParameterResolver consider(String possibleType, String parameterStr)
	{
		if (!this.paramRef.containsKey(parameterStr))
		{
			return this;
		}
		return this.consider(possibleType, this.paramRef.get(parameterStr));
	}
	public ParameterResolver consider(String possibleType, ParameterDef parameter)
	{
		String paramType = parameter.getType();
		if (paramType != null && !paramType.toLowerCase().equals(possibleType.toLowerCase()))
		{
			throw new RuntimeException("Parameter " + parameter + " already has a type, tried to match with: " + possibleType);
		}
		parameter.setType(possibleType);
		return this;
	}

	public boolean isComplete()
	{
		return this
			.paramRef
			.values()
			.stream()
			.noneMatch(x -> x.getType() == null)
		;
	}

	public List<ParameterDef> getUnresolvedParameters()
	{
		return this
			.paramRef
			.values()
			.stream()
			.filter(x -> x.getType() == null)
			.collect(Collectors.toList())
		;
	}
}
