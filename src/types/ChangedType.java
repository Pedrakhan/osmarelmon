package types;

/**
 * Interface for all changed types of OSMarelmon.
 * 
 * @author sebastian
 * 
 */
public interface ChangedType {

	/**
	 * Prints the ChangedType. Do not use toString().
	 * 
	 * @return A string representation of this ChangedType.
	 */
	public String print();

	/**
	 * Returns the state of the ChangedType.
	 * 
	 * @return the state of the ChangedType.
	 */
	public State getState();

}
