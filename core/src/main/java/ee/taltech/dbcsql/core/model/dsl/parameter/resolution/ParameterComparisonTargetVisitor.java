package ee.taltech.dbcsql.core.model.dsl.parameter.resolution;

import java.util.Collection;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;

public class ParameterComparisonTargetVisitor implements ComparisonTargetNodeVisitor<Void>
{
	private ParameterResolver resolver;
	private String possibleType;

	public ParameterComparisonTargetVisitor(String possibleType, ParameterResolver resolver)
	{
		this.possibleType = possibleType;
		this.resolver = resolver;
	}

	@Override
	public Void visit(CompositeComparisonTargetNode v)
	{
		visitSubnodes(v.getNodes());
		return null;
	}

	@Override
	public Void visit(LiteralComparisonTargetNode v)
	{
		this.resolver.consider(possibleType, v.getSymbol());
		return null;
	}

	@Override
	public Void visit(FunctionComparisonTargetNode v)
	{
		this.visitSubnodes(v.getParameters());
		return null;
	}

	private void visitSubnodes(Collection<ComparisonTargetNode> nodes)
	{
		for (ComparisonTargetNode c: nodes)
		{
			c.accept(this);
		}
	}
}
