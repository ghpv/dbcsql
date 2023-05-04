package ee.taltech.dbcsql.core.phase;

import ee.taltech.dbcsql.core.db.DBCommands;
import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.dsl.TargetPlatform;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableRegistry;
import ee.taltech.dbcsql.core.phase.input.NameMender;
import ee.taltech.dbcsql.core.phase.output.RestrictionRegistry;
import ee.taltech.dbcsql.core.phase.output.persistence.Persistence;

public class GenerationContext
{
	protected RestrictionRegistry restrictionRegistry = new RestrictionRegistry();
	protected VariableRegistry vars = new VariableRegistry();
	protected Persistence persistence;
	protected TargetPlatform platform;
	protected DatabaseDef databaseDef;
	protected DBCommands database;
	protected boolean returnLastPostcondition = false;
	protected boolean securityInvoker = false;
	protected NameMender parameterMender = new NameMender();
	protected NameMender functionMender = new NameMender();

	protected GenerationContext()
	{
		this.functionMender.setDisallowSQLKeywords(true);
	}

	public TargetPlatform getPlatform()
	{
		return this.platform;
	}
	public DatabaseDef getDatabaseDef()
	{
		return this.databaseDef;
	}
	public RestrictionRegistry getRestrictionRegistry()
	{
		return this.restrictionRegistry;
	}
	public Persistence getPersistence()
	{
		return this.persistence;
	}
	public VariableRegistry getVars()
	{
		return this.vars;
	}
	public DBCommands getDatabase()
	{
		return this.database;
	}
	public boolean isReturnLastPostcondition()
	{
		return returnLastPostcondition;
	}
	public void setReturnLastPostcondition(boolean returnLastPostcondition)
	{
		this.returnLastPostcondition = returnLastPostcondition;
	}

	public void setSecurityInvoker(boolean b)
	{
		this.securityInvoker = b;
	}
	public boolean getSecurityInvoker()
	{
		return this.securityInvoker;
	}

	public NameMender getParameterMender()
	{
		return this.parameterMender;
	}
	public void setParameterMender(NameMender mender)
	{
		this.parameterMender = mender;
	}

	public NameMender getFunctionMender()
	{
		return this.functionMender;
	}
	public void setFunctionMender(NameMender mender)
	{
		this.functionMender = mender;
	}
}
