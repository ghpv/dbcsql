package ee.taltech.dbcsql.core.phase.output.persistence;

public interface Persistence
{
	/**
	 * Let the persistence know that a new file is needed.
	 * This closes the previous file
	 */
	public Persistence startNew(String contractName);

	/**
	 * Let the persistence know that current input is done.
	 */
	public Persistence finished();

	public Persistence write(String s);
}
