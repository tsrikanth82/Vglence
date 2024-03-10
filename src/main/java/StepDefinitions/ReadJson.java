package StepDefinitions;

import com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.datatable.DataTable;

import java.io.IOException;
import java.util.Map;

public class ReadJson {
    private static final String JSON_FILE_EXTENSION = ".json";

    public String readJsonFromFile(String fileName) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        InputStream inputStream = ReadJson.class.getResourceAsStream("src/resources/request/" + fileName + JSON_FILE_EXTENSION );
//        return objectMapper.readTree(inputStream);
return null;


    }


    public static JsonNode updateJsonNode(JsonNode jsonNode, DataTable dataTable) {
        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            ((com.fasterxml.jackson.databind.node.ObjectNode) jsonNode).put(key, value);
        }
        return jsonNode;
    }
}
