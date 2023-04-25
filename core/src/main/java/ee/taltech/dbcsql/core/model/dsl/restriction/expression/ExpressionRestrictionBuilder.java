package ee.taltech.dbcsql.core.model.dsl.restriction.expression;

public class ExpressionRestrictionBuilder extends ExpressionRestrictionBuilderBase<ExpressionRestrictionBuilder>
{
	public ExpressionRestriction build()
	{
		return this.data;
	}
}
