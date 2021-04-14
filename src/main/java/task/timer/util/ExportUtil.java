package task.timer.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import task.timer.back.OS;

public class ExportUtil {

    public static String getDestDirectory() {
        String destPath = System.getProperty("user.home");

        if (OS.isWindows()) {
            destPath += File.separator + "Destkop";
        }

        destPath += File.separator + "Tasks" + File.separator;

        return destPath;
    }

    public static String getCsvName() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss")
            .format(new Date()) + ".csv";
    }
    
    public static String getDestPath() {
        return getDestDirectory() + getCsvName();
    }
}
