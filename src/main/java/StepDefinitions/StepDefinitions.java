//package StepDefinitions;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import components.EndpointComponent;
//import constants.VglenceConstants;
//import io.cucumber.datatable.DataTable;
//import io.cucumber.java.en.And;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import jakarta.validation.constraints.NotNull;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang3.NotImplementedException;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpHeaders;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpRequestBase;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.util.EntityUtils;
//import org.junit.Assert;
//
//import javax.ws.rs.core.assertNotNull;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.*;
//
//import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
//import static org.junit.Assert.*;
//
//
//@Slf4j
//public class StepDefinitions {
// //   jsonResponse = objectMapper.readTree(response);
//    private CloseableHttpResponse response;
//    private String jsonData;
//    public static final String REQUEST_BODY = "REQUEST_BODY";
//    Map<String, Object> cached;
//    private static final String JSON_FILE_EXTENSION = ".json";
//    private JsonNode jsonRequestMapped;
//
//    public ObjectMapper objectMapper;
//    private String fileContent;
//
//    private JsonNode jsonReadData;
//    private JsonNode jNode;
//
//
//    private EndpointComponent endpointComponent;
//
//    private StringBuilder jsonRequest;
//    private int httpResponseStatusCode;
//    private JsonNode jsonResponse;
//
//    public StepDefinitions() {this.cached = new HashMap<String, Object>();
//    }
//
////    @Given("I have a JSON request file named {string}")
////    public void i_have_a_json_request_file_named(String fileName) {
////        String filePath = "src/test/resources/request/" + fileName + ".json";
////        File file = new File(filePath);
////        StringBuilder jsonContent = new StringBuilder();
////        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
////            String line;
////            while ((line = reader.readLine()) != null) {
////                jsonData = String.valueOf(jsonContent.append(line));
////            }
////            System.out.println(jsonContent);
////        } catch (FileNotFoundException e) {
////            e.printStackTrace();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////
////        // return jsonContent.toString();
////    }
//
//    @Given("the following JSON data:")
//    public void theFollowingJSONData(DataTable dataTable) {
//        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            jsonData = jsonData.replaceAll("<" + entry.getKey() + ">", entry.getValue());
//        }
//        System.out.println(jsonData);
//    }
//
//
//    @And("set fields")
//    public void setFields(DataTable table) throws Exception {
//        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
//        JsonNode o = (JsonNode) this.getSaveObject(REQUEST_BODY);
//        System.out.println(o);
//
//        for(Map<String, String> columns : rows){
//            System.out.println(columns.toString());
//            ((ObjectNode)o).put( columns.get("FIELD"), columns.get("VALUE"));
//        }
//    }
//
//    @And("generate random fields to requestPayload")
//    public void generateRandomFieldsToRequestPayload() throws Exception {
//        var objectMapper = new ObjectMapper();
//        JsonNode o = (JsonNode) this.getSaveObject(REQUEST_BODY);
//
//        Iterator<Map.Entry<String, JsonNode>> p = o.fields();
//        while (p.hasNext()){
//            Map.Entry<String, JsonNode> node =p.next();
//
//            var field= node.getKey();
//            var value = node.getValue();
//
//            String v = this.resolveData(value.asText());
//            if(v==null){
//                ((ObjectNode)o).set(field, objectMapper.nullNode());
//            }else {
//                ((ObjectNode)o).put(field, v);
//            }
//        }
//        this.saveObject(REQUEST_BODY, o);
//    }
//
//    private String resolveData(String data) {
//        var v = data.replace("${/data:", "");
//        if(v.contains("null")){
//            return null;
//        }
//        if(v.contains("phone")){
//            return randomNumeric(10);
//        }
//
//        return data;
//    }
//
//    private void saveObject(@NotNull String key, Object value) {
//
//        if(value == null){
//            System.out.println("Key:" + key + " With value is null");
//        }else{
//            System.out.println("Saving Key:" + key + " Object:" + value.toString());
//        }
//        this.cached.put(key, value);
//    }
//
//    @When("I send a POST request to {string} with the JSON data")
//    public void iSendAPOSTRequestToWithTheJSONData(String endpoint) throws IOException {
//        HttpPost httpPost = new HttpPost(endpoint);
//        httpPost.setHeader("Content-Type", "application/json");
//
//        // Read JSON data from file and set as request entity
//        String jsonData = readJsonFromFile("request.json");
//        httpPost.setEntity(new StringEntity(jsonData, StandardCharsets.UTF_8));
//
//        //Execute POST request
//        response = httpClient.execute(httpPost);
//    }
//
//
//    public Object getSaveObject(String key) throws Exception {
//        var o = this.cached.get(key);
//        if (o == null) {
//            throw new Exception("please save the object in previous step");
//        }
//        return o;
//
//    }
//
//    @Then("the response should have HTTP code {int}")
//    public void theResponseShouldHaveHTTPCode(int expectedStatusCode) {
//        // Check if the response status code matches expectedStatusCode
//        int actualStatusCode = response.getStatusLine().getStatusCode();
 //      assertEquals(expectedStatusCode, actualStatusCode);
//    }
//
//    @And("the response message should contain {string}")
//    public void theResponseMessageShouldContain(String expectedMessage) throws IOException {
//        // Read response body and check if it contains the expected message
//        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//        assertTrue(responseBody.contains(expectedMessage));
//    }
//
//
//
//    private String readJsonFromFile(String fileName) {
//        String filePath = "src/resources/request/" + fileName;
//
//        try {
//            // Read the JSON file content and return as a String
//            return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
//        } catch (IOException e) {
//            // Handle the exception (e.g., log or throw a runtime exception)
//            e.printStackTrace();
//            throw new RuntimeException("Failed to read JSON file: " + fileName);
//        }
//    }
//    @When("I perform some action")
//    public void iPerformSomeAction() {
//        // Perform action with modified JSON data
//        System.out.println(jsonData);
//    }
//
//    @Given("a API call with {string} request")
//    public void aAPICallWithRequest(String requestType) throws IOException {
//        String fileName = requestType + JSON_FILE_EXTENSION;
//        fileContent = getJsonFile("request/" + fileName);
//        ObjectMapper objectMapper = new ObjectMapper();
//         jNode = objectMapper.readTree(fileContent);
//        System.out.println(jNode);
//
//
//    }
//
//    private String jsonData2;
//
////    @Given("I replace values in JSON with following data:")
////    public void iReplaceValuesInJSONWithFollowingData(DataTable dataTable) {
////        // Replace values in JSON string
//////        for (Map.Entry<String, String> entry : replacements.entrySet()) {
//////            String key = "<" + entry.getKey() + ">";
//////            String value = entry.getValue();
//////            fileContent = fileContent.replace(key, value);
//////        }
////
////
////        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
////        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
////            fileContent = fileContent.replaceAll("<" + entry.getKey() + ">", entry.getValue());
////        }
////        System.out.println("Final Updated JSON \n" + fileContent);
////    }
////
////    @Given("I have a JSON file named {string} request")
////    public void iHaveAJSONFileNamed(String fileName) throws IOException {
////        fileContent = getJsonFile(fileName);
////    }
//
//    @Given("I replace values with:")
//    public void iReplaceValuesWith(DataTable dataTable) {
//        Map<String, String> dataMap = dataTable.asMap(String.class, String.class);
//        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            // Replace values in JSON
//            replaceValue(jNode, key, value);
//            System.out.println(jNode);
//        }
//    }
//
//    private String getJsonFile(String filePath) throws IOException {
//        var writter = new StringWriter();
//        InputStream fileStram = this.getClass().getClassLoader().getResourceAsStream(filePath);
//        if (fileStram != null) {
//            IOUtils.copy(fileStram, writter, StandardCharsets.UTF_8);
//        } else {
//            Assert.fail("Unable to open file stream");
//        }
//        var contents = writter.toString();
//        writter.close();
//        return contents;
//    }
//
//    @When("a {} request is sent to the service {} endpoint")
//    public void aPOSTRequestIsSentToTheServicePlotsAPIEndpoint(String httpMethod, String endpointConfiguration) throws IOException {
//        HttpRequestBase httpRequest = buildRequest(httpMethod, endpointConfiguration);
//       executeHttpRequest(httpRequest);
//    }
//
//
//    private HttpRequestBase buildRequest(String httpMethod, String endpointConfiguration) throws IOException {
//        HttpRequestBase httpRequest;
//            Properties endpointConfigurationMap = new Properties();
//            String filePath = "src/main/resources/endpoint.properties";
//
//            try (InputStream inputStream = new FileInputStream(filePath)) {
//                endpointConfigurationMap.load(inputStream);
//            }
//        String servicePath = (String) endpointConfigurationMap.get(endpointConfiguration);
//        switch (httpMethod) {
//            case "POST":
//                httpRequest = new HttpPost((VglenceConstants.getTargetUrl() + servicePath));
//                break;
//            case "GET":
//                httpRequest = new HttpPost((VglenceConstants.getTargetUrl() + servicePath));
//                break;
//            default:
//                throw new NotImplementedException("Cannot resolve httpMethod");
//        }
//        return httpRequest;
//    }
//
//    private void executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
//        httpRequest.addHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON);
//        httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
//
//      System.out.println("Sending Request to Target Endpoint: " + httpRequest.toString());
//        if(httpRequest instanceof HttpPost ){
//            if (jNode != null){
//
//                System.out.println("Request Body: \n" + jNode);
//                ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(jNode.toString()));
//            }else{
//                assertNotNull(jNode);
//                System.out.println("Request Body: \n" + jNode);
//                ((HttpEntityEnclosingRequestBase) httpRequest).setEntity(new StringEntity(jNode.toString()));
//            }
//        }
//        CloseableHttpResponse closeableHttpResponse = httpClient.execute(httpRequest);
//        sleepThread(1000);
//        var httpEntity = closeableHttpResponse.getEntity();
//
//        var response = EntityUtils.toString(httpEntity);
//
//        httpResponseStatusCode = closeableHttpResponse.getStatusLine().getStatusCode();
//        jsonResponse = null;
//        if(StringUtils.isNoneEmpty(response)){
//           jsonResponse = objectMapper.readTree(response);
//            System.out.println("Response Body: \n" + response.toString());
//        }
//        closeableHttpResponse.close();
//    }
//
//    private void sleepThread(long milliseconds){
//        try{
//            Thread.sleep(milliseconds);
//
//        } catch (InterruptedException e) {
// //           log.info("Exception sleeping method");
//            Thread.currentThread().interrupt();
//            e.printStackTrace();
//        }
//    }
//
//
//    private JsonNode replaceValue(JsonNode node, String key, String value) {
//        if (node.isObject()) {
//            // If object, recursively replace value
//            node.fields().forEachRemaining(entry -> replaceValue(entry.getValue(), key, value));
//        } else if (node.isArray()) {
//            // If array, recursively replace value in each element
//            node.elements().forEachRemaining(element -> replaceValue(element, key, value));
//        }
////        else if (node.isValueNode() && node.asText().equals(key)) {
////            // If value node and matches key, replace value
////            return node.textNode(value);
////        }
//        return node;
//    }
//}
