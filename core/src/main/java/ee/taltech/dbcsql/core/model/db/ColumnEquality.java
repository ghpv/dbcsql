package ee.taltech.dbcsql.core.model.db;

import ee.taltech.dbcsql.core.model.Pair;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;

public class ColumnEquality extends Pair<ColumnDef, ComparisonTargetNode>
{
	public ColumnEquality(ColumnDef key, ComparisonTargetNode value)
	{
		super(key, value);
	}
}
