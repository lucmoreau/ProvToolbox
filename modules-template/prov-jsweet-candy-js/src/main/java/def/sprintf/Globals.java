package def.sprintf;

public class Globals {

	/**
	 * Example: <code>sprintf(
	 * "The first 4 letters of the English alphabet are: %s, %s, %s and %s",
	 * "a", "b", "c", "d")</code>
	 */
	public static native String sprintf(String format, Object... args);

	/**
	 * Example: <code>vprintf(
	 * "The first 4 letters of the English alphabet are: %s, %s, %s and %s", new
	 * String[] {"a", "b", "c", "d"})</code>
	 */
	public static native String vprintf(String format, Object[] args);

	

}
