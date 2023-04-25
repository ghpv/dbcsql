package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;

public class LiteralComparisonTargetNode implements ComparisonTargetNode
{
	private String symbol;

	public LiteralComparisonTargetNode()
	{
	}

	public LiteralComparisonTargetNode(String symbol)
	{
		this.setSymbol(symbol);
	}

	public LiteralComparisonTargetNode clone()
	{
		return new LiteralComparisonTargetNode(this.symbol);
	}

	public void setSymbol(String symbol)
	{
		this.symbol = symbol;
	}

	public String getSymbol()
	{
		return this.symbol;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof LiteralComparisonTargetNode))
		{
			return super.equals(obj);
		}
		LiteralComparisonTargetNode other = (LiteralComparisonTargetNode) obj;
		return true
			&& this.symbol.equals(other.symbol)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.symbol
		);
	}

	@Override
	public String toString()
	{
		return this.symbol;
	}

	@Override
	public <T> T accept(ComparisonTargetNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
