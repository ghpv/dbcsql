package ee.taltech.dbcsql.process.postgres;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNode;

public class ComparisonWhereNodeRenderer implements AttributeRenderer<ComparisonWhereNode>
{
	@Override
	public String toString(ComparisonWhereNode node, String arg1, Locale arg2)
	{
		return PostgresTemplates
			.instance()
			.getWhereNodeComparison()
			.add("d", node)
			.render()
		;
	}
}
