package ee.taltech.dbcsql.core.model.dsl.restriction.expression;

import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.Restriction;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ExpressionRestriction implements Restriction
{
	private ExpressionNode node;
	private VariableDef target;

	public ExpressionRestriction()
	{
	}

	public ExpressionRestriction(VariableDef target, ExpressionNode node)
	{
		this.setTarget(target);
		this.setNode(node);
	}

	@Override
	public ExpressionRestriction clone()
	{
		ExpressionRestriction copy = new ExpressionRestriction();
		copy.setNode(this.node.clone());
		copy.setTarget(this.getTarget());
		return copy;
	}

	public ExpressionNode getNode()
	{
		return node;
	}

	public VariableDef getTarget()
	{
		return target;
	}

	public void setTarget(VariableDef target)
	{
		this.target = target;
	}

	public void setNode(ExpressionNode node)
	{
		this.node = node;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ExpressionRestriction))
		{
			return super.equals(obj);
		}
		ExpressionRestriction other = (ExpressionRestriction) obj;
		return true
			&& this.node.equals(other.node)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.node
		);
	}

	@Override
	public <T> T accept(RestrictionVisitor<T> visitor)
	{
		return visitor.visit(this);
	}

	@Override
	public String toString()
	{
		return node.toString();
	}

	@Override
	public boolean canBeCombined()
	{
		return true;
	}

	@Override
	public boolean combineWith(Restriction obj)
	{
		if (!(obj instanceof ExpressionRestriction))
		{
			return false;
		}
		ExpressionRestriction other = (ExpressionRestriction) obj;
		ExpressionNodeCompositor compositor = new ExpressionNodeCompositor();
		this.node.accept(compositor);
		ExpressionNode n = other.node.accept(compositor);
		this.setNode(n);

		return true;
	}
}
