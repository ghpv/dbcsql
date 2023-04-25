package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal;

public class LiteralComparisonTargetNodeBuilderBase<BuilderT extends LiteralComparisonTargetNodeBuilderBase<BuilderT>>
{
	protected LiteralComparisonTargetNode data = new LiteralComparisonTargetNode();

	@SuppressWarnings("unchecked")
	public BuilderT withSymbol(String symbol)
	{
		this.data.setSymbol(symbol);
		return (BuilderT) this;
	}
}
