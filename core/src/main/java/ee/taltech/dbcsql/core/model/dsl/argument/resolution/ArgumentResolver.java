package ee.taltech.dbcsql.core.model.dsl.argument.resolution;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.db.ColumnDef;
import ee.taltech.dbcsql.core.model.db.ColumnEquality;
import ee.taltech.dbcsql.core.model.dsl.argument.ArgumentDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;

public class ArgumentResolver
{
	public Map<String, ArgumentDef> argRef = new HashMap<>();

	public void addArgumentReference(ArgumentDef arg)
	{
		this.argRef.put(arg.getAlias(), arg);
	}

	public ArgumentResolver consider(ColumnEquality eq)
	{
		return this.consider(eq.getKey().getType(), eq.getValue());
	}
	public ArgumentResolver consider(ComparisonExpressionNode expr)
	{
		return this.consider(
			expr.getColumnOwner().getTable().getColumn(expr.getColumn().getDSLName()),
			expr.getTargets()
		);
	}
	public ArgumentResolver consider(ColumnDef targettedColumn, Collection<ComparisonTargetNode> values)
	{
		String possibleType = targettedColumn.getType();
		for (ComparisonTargetNode s: values)
		{
			this.consider(possibleType, s);
		}
		return this;
	}
	public ArgumentResolver consider(String possibleType, ComparisonTargetNode compTarget)
	{
		ArgumentComparisonTargetVisitor targetVisitor = new ArgumentComparisonTargetVisitor(possibleType, this);
		compTarget.accept(targetVisitor);
		return this;
	}

	public ArgumentResolver consider(String possibleType, String argumentStr)
	{
		if (!this.argRef.containsKey(argumentStr))
		{
			return this;
		}
		return this.consider(possibleType, this.argRef.get(argumentStr));
	}
	public ArgumentResolver consider(String possibleType, ArgumentDef argument)
	{
		String argType = argument.getType();
		if (argType != null && !argType.toLowerCase().equals(possibleType.toLowerCase()))
		{
			throw new RuntimeException("Argument " + argument + " already has a type, tried to match with: " + possibleType);
		}
		argument.setType(possibleType);
		return this;
	}

	public boolean isComplete()
	{
		return this
			.argRef
			.values()
			.stream()
			.noneMatch(x -> x.getType() == null)
		;
	}

	public List<ArgumentDef> getUnresolvedArguments()
	{
		return this
			.argRef
			.values()
			.stream()
			.filter(x -> x.getType() == null)
			.collect(Collectors.toList())
		;
	}
}
