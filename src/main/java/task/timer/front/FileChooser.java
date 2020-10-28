package task.timer.front;

import task.timer.back.OS;

import javax.swing.*;
import java.io.File;

public class FileChooser {

    public static String chooseDir() {
        JFileChooser folder = new JFileChooser();
        folder.setCurrentDirectory(new File("."));
        folder.setDialogTitle("Selecione uma pasta");
        folder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        folder.setAcceptAllFileFilterUsed(false);

        String dir = null;

        if (folder.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            String caminho = folder.getSelectedFile().toString();
            if (OS.isWindows()) {
                caminho += File.separator;
            }

            dir = caminho;
        }

        return dir;
    }
}
