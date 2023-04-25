package ee.taltech.dbcsql.process.postgres.translate;

import java.util.List;
import java.util.stream.Collectors;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.composite.CompositeExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.unary.UnaryExpressionNode;
import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNodeBuilder;
import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNodeBuilder;
import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNodeBuilder;

public class RestrictionExpressionTranslator implements RestrictionExpressionNodeVisitor<WhereNode>
{
	@Override
	public WhereNode visit(CompositeExpressionNode node)
	{
		List<WhereNode> nodes = node
			.getNodes()
			.stream()
			.map(x -> x.accept(this))
			.collect(Collectors.toList())
		;

		return new CompositeWhereNodeBuilder()
			.withOperation(node.getOperation())
			.withNodes(nodes)
		.build()
		;
	}

	@Override
	public WhereNode visit(UnaryExpressionNode node)
	{
		return new UnaryWhereNodeBuilder()
			.withNegate(node.isNegate())
			.withNode(node.getNode().accept(this))
		.build()
		;
	}

	@Override
	public WhereNode visit(ComparisonExpressionNode node)
	{
		ComparisonTargetNodeStringifierVisitor v = new ComparisonTargetNodeStringifierVisitor();

		List<String> targets = node
			.getTargets()
			.stream()
			.map(x -> x.accept(v))
			.collect(Collectors.toList())
		;
		return new ComparisonWhereNodeBuilder()
			.withColumn(CommonTranslation.translateColumnName(node.getColumnOwner(), node.getColumn()))
			.withType(node.getType())
			.withTargets(targets)
		.build()
		;
	}
}
