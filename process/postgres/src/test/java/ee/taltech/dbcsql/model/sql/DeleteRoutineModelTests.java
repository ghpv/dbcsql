package ee.taltech.dbcsql.model.sql;

import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.GenerationRequestBuilder;
import ee.taltech.dbcsql.test.IntegrationTest;
import ee.taltech.dbcsql.test.SQLStatementAssertions;

public class DeleteRoutineModelTests extends IntegrationTest
{
	@Test
	public void testOutput()
	{
		GenerationRequest request =
			new GenerationRequestBuilder()
				.withContext(this.ctx)
				.makeContract()
					.withName("forget_car")
					.makeArgument()
						.withAlias("p_id")
						.withType("INTEGER")
					.build()
					.makeExistsPrecondition()
						.withTarget(this.testVar)
						.makeRestriction()
							.makeExpressionRestriction()
								.makeNode()
									.withOperation(BooleanOperation.AND)
									.extendWithUnary()
										.makeComparison()
											.withColumn(this.testVar, "id")
											.withType(ComparisonType.EQUALS)
											.withLiteralTargets("p_id")
										.build()
									.build()
								.build()
							.build()
						.build()
					.build()
					.makeDeletedPostcondition()
						.withTarget(this.testVar)
					.build()
				.build()
			.build();

		this.outGen.generate(request);
		String actual = this.persistence.getMemory().toString();

		String expected = """
		CREATE OR REPLACE FUNCTION forget_car
		(
			p_id INTEGER
		)
		RETURNS VOID
		LANGUAGE SQL SECURITY DEFINER
		SET SEARCH_PATH TO 'public', 'pg_temp'
		BEGIN ATOMIC
			DELETE FROM
				public.test AS a
			WHERE
				(
					a.id = p_id
				)
			;
		END;
		""";

		SQLStatementAssertions.assertStatementsEqual(actual, expected);
	}
}
