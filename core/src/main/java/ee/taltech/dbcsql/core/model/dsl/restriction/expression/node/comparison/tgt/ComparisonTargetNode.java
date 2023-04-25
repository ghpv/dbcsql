package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt;

public interface ComparisonTargetNode
{
	public <T> T accept(ComparisonTargetNodeVisitor<T> visitor);
	public ComparisonTargetNode clone();
}
