package task.timer.back;

/**
 * Helper class that find out current user OS
 */
public class OS {

    /**
     * Defines if current user instance it's running under Windows SO
     * @return true if Windows false otherwhise
     */
    public static boolean isWindows() {
	    String osName = System.getProperty("os.name");
	    return osName.toUpperCase().contains("WIN");
    }
}
