package task.timer.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import task.timer.back.OS;

public class ExportUtil {
    
    public static String getDestPath() {
        String destPath = System.getProperty("user.home");

        if (OS.isWindows()) {
            destPath += File.separator + "Destkop";
        }

        destPath += File.separator + "Tasks" + File.separator;

        // File name: AAAA-MM-DD-HH
        String fileName = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            .format(new Date()) + ".csv";

        return destPath + fileName;
    }
}
