package ee.taltech.dbcsql.core.model.dsl.restriction;

public interface Restriction
{
	public <T> T accept(RestrictionVisitor<T> visitor);

	/**
	 * return true if this Restriction be combined with others
	 */
	public boolean canBeCombined();

	/**
	 * Combine with other and return true if successful.
	 */
	public boolean combineWith(Restriction other);

	public Restriction clone();
}
