package com.chronicle.labs.api;

import com.chronicle.labs.api.ChronicleLabsApiClient;
import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.search.requests.SearchRequest;
import com.chronicle.labs.api.types.EventListResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchWireTest {
    private MockWebServer server;
    private ChronicleLabsApiClient client;
    private ObjectMapper objectMapper = ObjectMappers.JSON_MAPPER;
    @BeforeEach
    public void setup() throws Exception {
        server = new MockWebServer();
        server.start();
        client = ChronicleLabsApiClient.builder()
            .url(server.url("/").toString())
            .token("test-token")
            .build();
    }
    @AfterEach
    public void teardown() throws Exception {
        server.shutdown();
    }
    @Test
    public void testEvents() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"data\":[{\"event\":{\"event_id\":\"evt_18C8B8D284059DAF4a74c7cc2a3a669d\",\"org_id\":\"org_id\",\"source\":\"stripe\",\"topic\":\"payments\",\"event_type\":\"payment_intent.succeeded\",\"event_time\":\"2024-01-15T09:30:00Z\",\"ingestion_time\":\"2024-01-15T09:30:00Z\",\"media\":{\"media_type\":\"image/jpeg\",\"size_bytes\":1000000},\"entity_refs\":[{\"entity_type\":\"customer\",\"entity_id\":\"cust_123\"}]},\"entity_refs\":[{\"event_id\":\"event_id\",\"entity_type\":\"customer\",\"entity_id\":\"cust_123\",\"created_by\":\"ingestion\",\"created_at\":\"2024-01-15T09:30:00Z\"}],\"search_distance\":1.1}],\"next_cursor\":\"next_cursor\"}"));
        EventListResponse response = client.search().events(
            SearchRequest
                .builder()
                .query("query")
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{\n"
            + "  \"query\": \"query\"\n"
            + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type")) discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind")) discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }
        
        if (!actualJson.isNull()) {
            Assertions.assertTrue(actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(), "request should be a valid JSON value");
        }
        
        if (actualJson.isArray()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Array should have valid size");
        }
        if (actualJson.isObject()) {
            Assertions.assertTrue(actualJson.size() >= 0, "Object should have valid field count");
        }
        
        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
            + "{\n"
            + "  \"data\": [\n"
            + "    {\n"
            + "      \"event\": {\n"
            + "        \"event_id\": \"evt_18C8B8D284059DAF4a74c7cc2a3a669d\",\n"
            + "        \"org_id\": \"org_id\",\n"
            + "        \"source\": \"stripe\",\n"
            + "        \"topic\": \"payments\",\n"
            + "        \"event_type\": \"payment_intent.succeeded\",\n"
            + "        \"event_time\": \"2024-01-15T09:30:00Z\",\n"
            + "        \"ingestion_time\": \"2024-01-15T09:30:00Z\",\n"
            + "        \"media\": {\n"
            + "          \"media_type\": \"image/jpeg\",\n"
            + "          \"size_bytes\": 1000000\n"
            + "        },\n"
            + "        \"entity_refs\": [\n"
            + "          {\n"
            + "            \"entity_type\": \"customer\",\n"
            + "            \"entity_id\": \"cust_123\"\n"
            + "          }\n"
            + "        ]\n"
            + "      },\n"
            + "      \"entity_refs\": [\n"
            + "        {\n"
            + "          \"event_id\": \"event_id\",\n"
            + "          \"entity_type\": \"customer\",\n"
            + "          \"entity_id\": \"cust_123\",\n"
            + "          \"created_by\": \"ingestion\",\n"
            + "          \"created_at\": \"2024-01-15T09:30:00Z\"\n"
            + "        }\n"
            + "      ],\n"
            + "      \"search_distance\": 1.1\n"
            + "    }\n"
            + "  ],\n"
            + "  \"next_cursor\": \"next_cursor\"\n"
            + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(jsonEquals(expectedResponseNode, actualResponseNode), "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type")) discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type")) discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind")) discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }
        
        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(), "response should be a valid JSON value");
        }
        
        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }
    
    /**
     * Compares two JsonNodes with numeric equivalence and null safety.
     * For objects, checks that all fields in 'expected' exist in 'actual' with matching values.
     * Allows 'actual' to have extra fields (e.g., default values added during serialization).
     */
    private boolean jsonEquals(JsonNode expected, JsonNode actual) {
        if (expected == null && actual == null) return true;
        if (expected == null || actual == null) return false;
        if (expected.equals(actual)) return true;
        if (expected.isNumber() && actual.isNumber()) return Math.abs(expected.doubleValue() - actual.doubleValue()) < 1e-10;
        if (expected.isObject() && actual.isObject()) {
            java.util.Iterator<java.util.Map.Entry<String, JsonNode>> iter = expected.fields();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonNode> entry = iter.next();
                JsonNode actualValue = actual.get(entry.getKey());
                if (actualValue == null) { if (!entry.getValue().isNull()) return false; }
                else if (!jsonEquals(entry.getValue(), actualValue)) return false;
            }
            return true;
        }
        if (expected.isArray() && actual.isArray()) {
            if (expected.size() != actual.size()) return false;
            for (int i = 0; i < expected.size(); i++) {
                if (!jsonEquals(expected.get(i), actual.get(i))) return false;
            }
            return true;
        }
        return false;
    }
}
