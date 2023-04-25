package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;

public interface ComparisonTargetNodeVisitor <T>
{
	public T visit(CompositeComparisonTargetNode v);
	public T visit(LiteralComparisonTargetNode v);
	public T visit(FunctionComparisonTargetNode v);
}
