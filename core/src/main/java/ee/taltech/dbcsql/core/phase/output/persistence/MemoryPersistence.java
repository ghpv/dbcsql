package ee.taltech.dbcsql.core.phase.output.persistence;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MemoryPersistence implements Persistence
{
	private static final int INITIAL_CAPACITY_IN_BYTES = 5 * 1024 * 1024;
	private static final String EMPTY = null;

	private ByteArrayOutputStream memory = null;
	private PrintWriter writer = null;

	public ByteArrayOutputStream getMemory()
	{
		return memory;
	}

	@Override
	public Persistence startNew(String contractName)
	{
		this.finished();
		this.memory = new ByteArrayOutputStream(INITIAL_CAPACITY_IN_BYTES);
		this.writer = new PrintWriter(new OutputStreamWriter(this.memory));
		return this;
	}
	public Persistence startNew()
	{
		return this.startNew(EMPTY);
	}


	@Override
	public Persistence finished()
	{
		if (this.writer != null)
		{
			this.writer.close();
		}
		return this;
	}

	@Override
	public Persistence write(String s)
	{
		this.writer.write(s);
		return this;
	}
}
