package ee.taltech.dbcsql.core.model.sql.where.composite;

import java.util.List;

import ee.taltech.dbcsql.core.model.dsl.generic.CompositeNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.WhereNodeVisitor;

public class CompositeWhereNode
	extends CompositeNode<WhereNode, BooleanOperation>
	implements WhereNode
{
	public CompositeWhereNode()
	{
		super(BooleanOperation.AND);
	}

	public CompositeWhereNode(List<WhereNode> nodes, BooleanOperation mode)
	{
		this();
		this.setNodes(nodes);
		this.setOperation(mode);
	}

	@Override
	public <T> T accept(WhereNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
