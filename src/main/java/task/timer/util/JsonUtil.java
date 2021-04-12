package task.timer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {
    
    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class.getName());

    public static String getJsonDbFileName() {
        // Estrutura: [{programa: PW00173, nome: Nome do programa}]
        return "Programas.json";
    }

    private static Map<String, String> getJsonMap() {
        final Map<String, String> programas = new HashMap<>();
        
        String baseDir = ExportUtil.getDestDirectory();
        File db = new File(baseDir + File.separator + JsonUtil.getJsonDbFileName());
        if (!db.exists()) {
            logger.info("Arquivo JSON DB {} não existe!", db.getAbsolutePath());
            return programas;
        }

        try {
            String content = Files.readString(db.toPath());
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(content);
            if (obj instanceof JSONArray) {
                JSONArray jsonArray = JSONArray.class.cast(obj);

                for (int i=0; i<jsonArray.size(); i++) {
                    Object objPrograma = jsonArray.get(i);

                    if (objPrograma instanceof JSONObject) {
                        JSONObject json = JSONObject.class.cast(objPrograma);
                        Object programa = json.get("programa");
                        Object nome = json.get("nome");

                        if (programa != null && nome != null) {
                            programas.put(String.valueOf(programa), String.valueOf(nome));
                        }
                    }
                }
            }
        } catch (IOException | ParseException fne) {
            fne.printStackTrace();
        }

        return programas;
    }

    private static void salvarJson(Map<String, String> map) {
        String baseDir = ExportUtil.getDestDirectory();
        File db = new File(baseDir + File.separator + JsonUtil.getJsonDbFileName());
        if (!db.exists()) {
            logger.info("Arquivo JSON DB {} não existe, criando...", db.getAbsolutePath());
            try {
                if (!db.createNewFile()) {
                    logger.info("Falha ao criar arquivo JSON! Saindo sem sucesso!");
                } else {
                    logger.info("Arquivo criado com sucesso!");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }

        StringBuilder jsonContent = new StringBuilder("[");
        int items = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (items > 0) {
                jsonContent.append(",");
            }

            jsonContent.append("{");
            jsonContent.append("\"programa\":\"").append(entry.getKey()).append("\"");
            jsonContent.append(",\"nome\":\"").append(entry.getValue()).append("\"");
            jsonContent.append("}");
            items++;
        }

        jsonContent.append("]");

        try {
            JSONParser parser = new JSONParser();

            // Tenta fazer parse para ver se é válido!
            Object obj = parser.parse(jsonContent.toString());
            if (obj == null) {
                logger.info("Problema ao fazer parse antes de gravar, abortando!");
            }

            Files.write(db.toPath(), jsonContent.toString().getBytes());

            logger.info("Salvo com sucesso!");
        } catch (IOException | ParseException fne) {
            fne.printStackTrace();
        }
    }

    public static String getNome(String programa) {
        final Map<String, String> programas = getJsonMap();
        return programas.getOrDefault(programa, "");
    }

    public static void salvarNome(String programa, String nome) {
        final Map<String, String> programas = getJsonMap();

        String anterior = programas.put(programa, nome);
        logger.info("Substituindo nome do programa={} de={} para={}", programa, anterior, nome);

        salvarJson(programas);
    }
}
