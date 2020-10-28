package task.timer.app;

public class OS {
    public static boolean isWindows() {
	    String osName = System.getProperty("os.name");
	    return osName.toUpperCase().contains("WIN");
    }
}
