package ee.taltech.dbcsql.process.postgres;

import java.util.Locale;

import org.stringtemplate.v4.AttributeRenderer;

import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNode;

public class CompositeWhereNodeRenderer implements AttributeRenderer<CompositeWhereNode>
{
	@Override
	public String toString(CompositeWhereNode node, String arg1, Locale arg2)
	{
		return PostgresTemplates
			.instance()
			.getWhereNodeComposite()
			.add("d", node)
			.render()
		;
	}
}
