package ee.taltech.dbcsql.process.postgres;

import java.net.URL;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import ee.taltech.dbcsql.core.model.sql.Statement;
import ee.taltech.dbcsql.core.model.sql.where.comparison.ComparisonWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.composite.CompositeWhereNode;
import ee.taltech.dbcsql.core.model.sql.where.unary.UnaryWhereNode;

public class PostgresTemplates
{
	private static final String STG_RESOURCE = "postgres.stg";
	private static final String FUNCTION = "func";
	private static final String STATEMENT_DELETE = "deleted";
	private static final String STATEMENT_INSERT = "inserted";
	private static final String STATEMENT_UPDATE = "updated";
	private static final String WHERE_NODE_UNARY = "wn_unary";
	private static final String WHERE_NODE_COMPARISON = "wn_comparison";
	private static final String WHERE_NODE_COMPOSITE = "wn_composite";
	private STGroup templates;
	public static final PostgresTemplates INSTANCE = new PostgresTemplates();

	public static PostgresTemplates instance()
	{
		return INSTANCE;
	}

	private PostgresTemplates()
	{
		URL stg = this
			.getClass()
			.getClassLoader()
			.getResource(STG_RESOURCE)
		;
		this.templates = new STGroupFile(stg);
		templates.registerRenderer(Statement.class, new StatementRenderer());
		templates.registerRenderer(UnaryWhereNode.class, new UnaryWhereNodeRenderer());
		templates.registerRenderer(CompositeWhereNode.class, new CompositeWhereNodeRenderer());
		templates.registerRenderer(ComparisonWhereNode.class, new ComparisonWhereNodeRenderer());
	}

	public ST getFunction()
	{
		return this.get(FUNCTION);
	}

	public ST getDeletedStatement()
	{
		return this.get(STATEMENT_DELETE);
	}

	public ST getInsertedStatement()
	{
		return this.get(STATEMENT_INSERT);
	}

	public ST getUpdatedStatement()
	{
		return this.get(STATEMENT_UPDATE);
	}

	public ST getWhereNodeComposite()
	{
		return this.get(WHERE_NODE_COMPOSITE);
	}

	public ST getWhereNodeComparison()
	{
		return this.get(WHERE_NODE_COMPARISON);
	}

	public ST getWhereNodeUnary()
	{
		return this.get(WHERE_NODE_UNARY);
	}

	private ST get(String target)
	{
		return this.templates.getInstanceOf(target);
	}
}
