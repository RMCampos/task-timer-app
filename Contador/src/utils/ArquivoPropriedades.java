/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Ricardo
 */
public class ArquivoPropriedades {
    private static final String caminho = "C:\\kugel\\KTaximetro.properties";
    public static final String propUsuarioApontamento = "usuario_apontamento";
    public static final String propUsuarioSite = "usuario_site_kugel";
    public static final String propSenhaSite = "senha_site_kugel";
    
    public static Properties getProperties() {
        Properties prop = new Properties();
        File file = new File(caminho);
        
        if (!file.exists() || !file.canRead()) {
            return null;
        }
        
        FileInputStream input;
        try {
            input = new FileInputStream(file);
            prop.load(input);
            input.close();
            return prop;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void criarArquivoProperties() {
        try {
            FileWriter fw = new FileWriter(new File(caminho));
            fw.append("usuario_apontamento=\r\n");
            fw.append("usuario_site_kugel=\r\n");
            fw.append("senha_site_kugel=\r\n");
            fw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao criar arquivo properties!");
        }
    }
    
    public static void salvarPropriedade(String prop, String valor) {
        try {
            FileInputStream in = new FileInputStream(caminho);
            Properties props = new Properties();
            props.load(in);
            in.close();
            
            FileOutputStream out = new FileOutputStream(caminho);
            props.setProperty(prop, valor);
            props.store(out, null);
            out.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
