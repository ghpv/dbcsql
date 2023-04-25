package ee.taltech.dbcsql.model.dsl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.testng.Assert;
import org.testng.annotations.Test;

import ee.taltech.dbcsql.core.model.db.DatabaseDef;
import ee.taltech.dbcsql.core.model.db.TableDef;
import ee.taltech.dbcsql.core.model.dsl.ContractDef;
import ee.taltech.dbcsql.core.model.dsl.ContractDefBuilder;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.BooleanOperation;
import ee.taltech.dbcsql.core.model.dsl.restriction.expression.node.comparison.ComparisonType;
import ee.taltech.dbcsql.core.model.dsl.variable.VariableDef;
import ee.taltech.dbcsql.core.phase.GenerationRequest;
import ee.taltech.dbcsql.core.phase.input.InputPhase;
import ee.taltech.dbcsql.db.postgres.PostgresPlatform;

public class RestrictionExpressionParsingTests
{
	@Test
	public void parseBoolTree()
	{
		InputStream context = new ByteArrayInputStream("""
		table test
		{
			col1 INTEGER;
			col2 INTEGER;
			col3 INTEGER;
			col4 VARCHAR;
			col5 VARCHAR;
		};
		""".getBytes());
		InputStream contract = new ByteArrayInputStream("""
		operation test
		{
		}
		preconditions
		{
			exists test a((col1 > 5 and col2 < 9 and col3 in (5, 6)) or (!(col4 <= 'wow') or col5 >= 'yes'));
		}
		postconditions
		{
		}
		""".getBytes());

		GenerationRequest req = new InputPhase()
			.makeGenerationContext()
				.withPlatform(new PostgresPlatform())
				.withContextFromStream(context)
				.makeCombinedPersistence()
					.withPersistenceInMemory()
				.build()
			.build()
			.withContractStream(contract)
		.finishInput();

		ContractDef actual = req.getContracts().get(0);
		DatabaseDef db = req.getContext().getDatabaseDef();
		TableDef table = db.getTableDSL("test");

		VariableDef v = new VariableDef("a", table);
		ContractDef expected =
			new ContractDefBuilder()
				.withName("test")
				.makeExistsPrecondition()
					.withTarget(v)
					.makeRestriction()
						.makeExpressionRestriction()
							.makeNode()
								.withOperation(BooleanOperation.OR)
								.extendWithComposite()
									.withOperation(BooleanOperation.AND)
									.extendWithComparison()
										.withColumn(v, "col1")
										.withType(ComparisonType.GREATER)
										.withLiteralTargets("5")
									.build()
									.extendWithComparison()
										.withColumn(v, "col2")
										.withType(ComparisonType.LESS)
										.withLiteralTargets("9")
									.build()
									.extendWithComparison()
										.withColumn(v, "col3")
										.withType(ComparisonType.IN)
										.withLiteralTargets("5", "6")
									.build()
								.build()
								.extendWithUnary()
									.withNegate()
									.makeComparison()
										.withColumn(v, "col4")
										.withType(ComparisonType.LESS_EQUALS)
										.withLiteralTargets("'wow'")
									.build()
								.build()
								.extendWithComparison()
									.withColumn(v, "col5")
									.withType(ComparisonType.GREATER_EQUALS)
									.withLiteralTargets("'yes'")
								.build()
							.build()
						.build()
					.build()
				.build()
			.build()
		;

		Assert.assertEquals(actual, expected);
	}
}
