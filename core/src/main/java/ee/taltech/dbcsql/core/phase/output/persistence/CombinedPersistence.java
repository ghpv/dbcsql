package ee.taltech.dbcsql.core.phase.output.persistence;

import java.util.LinkedList;
import java.util.List;

public class CombinedPersistence implements Persistence
{
	private List<Persistence> persistences = new LinkedList<>();

	public CombinedPersistence()
	{
	}
	public CombinedPersistence(List<Persistence> persistences)
	{
		this.persistences = persistences;
	}

	public void addPersistence(Persistence p)
	{
		this.persistences.add(p);
	}

	@Override
	public Persistence startNew(String contractName)
	{
		this
			.persistences
			.forEach(x -> x.startNew(contractName));
		return this;
	}

	@Override
	public Persistence finished()
	{
		this
			.persistences
			.forEach(x -> x.finished());
		return this;
	}

	@Override
	public Persistence write(String s)
	{
		this
			.persistences
			.forEach(x -> x.write(s));
		return this;
	}
}
