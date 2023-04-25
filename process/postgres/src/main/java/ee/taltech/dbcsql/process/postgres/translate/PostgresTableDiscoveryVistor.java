package ee.taltech.dbcsql.process.postgres.translate;

import java.util.function.Consumer;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class PostgresTableDiscoveryVistor implements RestrictionExpressionNodeVisitor<Void>
{
	private Consumer<VariableDef> callback;

	public PostgresTableDiscoveryVistor(Consumer<VariableDef> callback)
	{
		this.callback = callback;
	}

	@Override
	public Void visit(ComparisonExpressionNode node)
	{
		this.callback.accept(node.getColumnOwner());
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
		node.getNode().accept(this);
		return null;
	}
}
