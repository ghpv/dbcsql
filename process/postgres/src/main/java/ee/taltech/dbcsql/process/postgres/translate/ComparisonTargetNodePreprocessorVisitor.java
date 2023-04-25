package ee.taltech.dbcsql.process.postgres.translate;

import java.util.Collection;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.ComparisonTargetNodeVisitor;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.composite.CompositeComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.func.FunctionComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.tgt.literal.LiteralComparisonTargetNode;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;

public class ComparisonTargetNodePreprocessorVisitor implements ComparisonTargetNodeVisitor<Void>
{
	private VariableDef target;

	public ComparisonTargetNodePreprocessorVisitor(VariableDef target)
	{
		this.target = target;
	}

	@Override
	public Void visit(CompositeComparisonTargetNode v)
	{
		this.processSubnodes(v.getNodes());
		return null;
	}

	@Override
	public Void visit(LiteralComparisonTargetNode v)
	{
		if (target.getTable().hasColumnDSL(v.getSymbol()))
		{
			v.setSymbol(CommonTranslation.translateColumnName(
				target,
				target.getTable().getColumnDSL(v.getSymbol())
			));
		}
		return null;
	}

	@Override
	public Void visit(FunctionComparisonTargetNode v)
	{
		this.processSubnodes(v.getArguments());
		return null;
	}

	private void processSubnodes(Collection<ComparisonTargetNode> nodes)
	{
		for (ComparisonTargetNode n: nodes)
		{
			n.accept(this);
		}
	}
}
