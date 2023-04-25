package ee.taltech.dbcsql.core.phase.input;

import org.antlr.v4.runtime.tree.ParseTreeListener;

public interface DSLListener <T> extends ParseTreeListener
{
	T getData();
}
