package ee.taltech.dbcsql.core.model.sql.where.comparison;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.sql.where.WhereNode;
import ee.taltech.dbcsql.core.model.sql.where.WhereNodeVisitor;

public class ComparisonWhereNode implements WhereNode
{
	private String column;
	private ComparisonType type;
	private StringTargets targets;

	public ComparisonWhereNode()
	{
	}

	public ComparisonWhereNode(String col, ComparisonType type, List<String> targets)
	{
		this.setColumn(col);
		this.setType(type);
		this.setTargets(targets);
	}

	public void setColumn(String column)
	{
		this.column = column;
	}

	public void setType(ComparisonType type)
	{
		this.type = type;
		this.updateValueType();
	}

	public void addTarget(String target)
	{
		this.targets.add(target);
	}

	public void setTargets(Collection<String> targets)
	{
		this.targets = new StringTargets(targets);
		this.updateValueType();
	}

	public String getColumn()
	{
		return column;
	}

	public ComparisonType getType()
	{
		return type;
	}

	public StringTargets getTargets()
	{
		return targets;
	}

	private void updateValueType()
	{
		if (this.targets == null)
		{
			return;
		}
		switch (this.type)
		{
			case IN:
			case NOT_IN: // fallthrough
				this.targets.setSingularValue(false);
				break;
			default:
				this.targets.setSingularValue(true);
				break;
		}
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ComparisonWhereNode))
		{
			return super.equals(obj);
		}
		ComparisonWhereNode other = (ComparisonWhereNode) obj;
		return true
			&& this.column.equals(other.column)
			&& this.type.equals(other.type)
			&& this.targets.equals(other.targets)
		;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(
			this.column,
			this.type,
			this.targets.hashCode()
		);
	}

	@Override
	public String toString()
	{
		return new StringBuilder()
			.append(this.column)
			.append(this.type.toString())
			.append(this.targets)
			.toString()
		;
	}

	@Override
	public <T> T accept(WhereNodeVisitor<T> visitor)
	{
		return visitor.visit(this);
	}
}
