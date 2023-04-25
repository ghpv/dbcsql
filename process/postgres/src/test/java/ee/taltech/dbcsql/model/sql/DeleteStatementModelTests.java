package ee.taltech.dbcsql.model.sql;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostcondition;
import ee.taltech.dbcsql.core.model.dsl.post.deleted.DeletedPostconditionBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDef;
import ee.taltech.dbcsql.core.model.dsl.restriction.RestrictionDefBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.test.IntegrationTest;
import ee.taltech.dbcsql.test.SQLStatementAssertions;

public class DeleteStatementModelTests extends IntegrationTest
{
	@Test
	public void testDeleteAllFromModel()
	{
		DeletedPostcondition condition = new DeletedPostconditionBuilder()
			.withTarget(this.testVar)
			.build()
		;

		String actual = this.generateCondition(condition);
		String expected = "DELETE FROM public.test as a ;";

		SQLStatementAssertions.assertStatementsEqual(actual, expected);
	}

	@Test
	public void testDeleteWithRestrictions()
	{
		DeletedPostcondition condition = new DeletedPostconditionBuilder()
			.withTarget(this.testVar)
			.build()
		;
		RestrictionDef restriction =
			new RestrictionDefBuilder()
				.makeExpressionRestriction()
					.makeNode()
						.withOperation(BooleanOperation.AND)
						.extendWithUnary()
							.makeComparison()
								.withColumn(new VariableDef("a", this.testTable), "id")
								.withType(ComparisonType.EQUALS)
								.withLiteralTargets("5")
							.build()
						.build()
					.build()
				.build()
			.build()
		;
		this.reg.extendRestriction(restriction);

		String actual = this.generateCondition(condition);
		String expected = "DELETE FROM public.test as a WHERE ( a.id = 5 ) ;";

		SQLStatementAssertions.assertStatementsEqual(actual, expected);
	}
}
