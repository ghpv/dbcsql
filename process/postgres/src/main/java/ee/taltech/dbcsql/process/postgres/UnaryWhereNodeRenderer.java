package ee.taltech.dbcsql.process.postgres;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNode;

public class UnaryWhereNodeRenderer implements AttributeRenderer<UnaryWhereNode>
{
	@Override
	public String toString(UnaryWhereNode node, String arg1, Locale arg2)
	{
		return PostgresTemplates
			.instance()
			.getWhereNodeUnary()
			.add("d", node)
			.render()
		;
	}
}
