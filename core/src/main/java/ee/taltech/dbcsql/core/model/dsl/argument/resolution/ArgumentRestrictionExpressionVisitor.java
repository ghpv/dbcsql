package ee.taltech.dbcsql.core.model.dsl.argument.resolution;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNode;

public class ArgumentRestrictionExpressionVisitor implements RestrictionExpressionNodeVisitor<Void>
{
	private ArgumentResolver resolver;

	public ArgumentRestrictionExpressionVisitor(ArgumentResolver resolver)
	{
		this.resolver = resolver;
	}

	@Override
	public Void visit(ComparisonExpressionNode node)
	{
		resolver.consider(node);
		return null;
	}

	@Override
	public Void visit(CompositeExpressionNode node)
	{
		for (ExpressionNode n: node.getNodes())
		{
			n.accept(this);
		}
		return null;
	}

	@Override
	public Void visit(UnaryExpressionNode node)
	{
		node
			.getNode()
			.accept(this)
		;
		return null;
	}
}
