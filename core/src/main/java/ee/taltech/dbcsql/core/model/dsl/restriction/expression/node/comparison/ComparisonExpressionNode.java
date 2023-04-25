package ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.db.AliasedName;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.ExpressionNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.RestrictionExpressionNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ComparisonExpressionNode implements ExpressionNode
{
	private VariableDef columnOwner;
	private AliasedName column;
	private ComparisonType type;
	private List<ComparisonTargetNode> targets = new LinkedList<>();

	public ComparisonExpressionNode()
	{
	}

	public ComparisonExpressionNode(VariableDef columnOwner, AliasedName column, ComparisonType type, List<ComparisonTargetNode> targets)
	{
		this.setColumn(columnOwner, column);
		this.setType(type);
		this.setTargets(targets);
	}

	public ComparisonExpressionNode clone()
	{
		return new ComparisonExpressionNode(this.columnOwner, this.column, this.type, new LinkedList<>(this.targets));
	}

	public void setColumn(VariableDef columnOwner, AliasedName column)
	{
		this.columnOwner = columnOwner;
		this.column = column;
	}

	public void setType(ComparisonType type)
	{
		this.type = type;
	}

	public void addTarget(ComparisonTargetNode target)
	{
		this.targets.add(target);
	}

	public void setTargets(List<ComparisonTargetNode> targets)
	{
		this.targets = targets;
	}

	public VariableDef getColumnOwner()
	{
		return columnOwner;
	}
	public AliasedName getColumn()
	{
		return column;
	}
	public String getColumnDSL()
	{
		return column.getDSLName().getName();
	}
	public String getColumnDB()
	{
		return column.getDBName().getName();
	}

	public ComparisonType getType()
	{
		return type;
	}

	public List<ComparisonTargetNode> getTargets()
	{
		return targets;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ComparisonExpressionNode))
		{
			return super.equals(obj);
		}
		ComparisonExpressionNode other = (ComparisonExpressionNode) obj;
		return true
			&& this.columnOwner.getAlias().equals(other.columnOwner.getAlias())
			&& this.column.equals(other.column)
			&& this.type.equals(other.type)
			&& this.targets.equals(other.targets)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.columnOwner.getAlias(),
			this.column,
			this.type,
			this.targets.hashCode()
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.columnOwner.getAlias())
			.append(this.column)
			.append(this.type.toString())
			.append(this.targets)
			.toString()
		;
	}

	@Override
	public <T> T accept(RestrictionExpressionNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
