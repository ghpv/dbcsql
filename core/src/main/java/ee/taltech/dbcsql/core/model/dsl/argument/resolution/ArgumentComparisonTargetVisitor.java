package ee.taltech.dbcsql.core.model.dsl.argument.resolution;

import java.util.Collection;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;

public class ArgumentComparisonTargetVisitor implements ComparisonTargetNodeVisitor<Void>
{
	private ArgumentResolver resolver;
	private String possibleType;

	public ArgumentComparisonTargetVisitor(String possibleType, ArgumentResolver resolver)
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
		this.visitSubnodes(v.getArguments());
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
