package com.chronicle.labs.api;

import com.chronicle.labs.api.ChronicleLabsApiClient;
import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.events.requests.QueryEventsRequest;
import com.chronicle.labs.api.resources.events.requests.StreamEventsRequest;
import com.chronicle.labs.api.types.EventListResponse;
import com.chronicle.labs.api.types.EventResult;
import com.chronicle.labs.api.types.IngestRequest;
import com.chronicle.labs.api.types.IngestResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Iterable;
import java.util.Arrays;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EventsWireTest {
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
    public void testQueryEvents() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"data\":[{\"event\":{\"event_id\":\"evt_18C8B8D284059DAF4a74c7cc2a3a669d\",\"org_id\":\"org_id\",\"source\":\"stripe\",\"topic\":\"payments\",\"event_type\":\"payment_intent.succeeded\",\"event_time\":\"2024-01-15T09:30:00Z\",\"ingestion_time\":\"2024-01-15T09:30:00Z\",\"media\":{\"media_type\":\"image/jpeg\",\"size_bytes\":1000000},\"entity_refs\":[{\"entity_type\":\"customer\",\"entity_id\":\"cust_123\"}]},\"entity_refs\":[{\"event_id\":\"event_id\",\"entity_type\":\"customer\",\"entity_id\":\"cust_123\",\"created_by\":\"ingestion\",\"created_at\":\"2024-01-15T09:30:00Z\"}],\"search_distance\":1.1}],\"next_cursor\":\"next_cursor\"}"));
        EventListResponse response = client.events().queryEvents(
            QueryEventsRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());
        
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
    @Test
    public void testIngestEvent() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"event_ids\":[\"event_ids\"],\"count\":1}"));
        IngestResponse response = client.events().ingestEvent(
            IngestRequest
                .builder()
                .source("my-agent")
                .topic("conversations")
                .eventType("message.sent")
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{\n"
            + "  \"source\": \"my-agent\",\n"
            + "  \"topic\": \"conversations\",\n"
            + "  \"event_type\": \"message.sent\"\n"
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
            + "  \"event_ids\": [\n"
            + "    \"event_ids\"\n"
            + "  ],\n"
            + "  \"count\": 1\n"
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
    @Test
    public void testIngestEventBatch() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"event_ids\":[\"event_ids\"],\"count\":1}"));
        IngestResponse response = client.events().ingestEventBatch(
            Arrays.asList(
                IngestRequest
                    .builder()
                    .source("my-agent")
                    .topic("conversations")
                    .eventType("message.sent")
                    .build()
            )
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "[\n"
            + "  {\n"
            + "    \"source\": \"my-agent\",\n"
            + "    \"topic\": \"conversations\",\n"
            + "    \"event_type\": \"message.sent\"\n"
            + "  }\n"
            + "]";
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
            + "  \"event_ids\": [\n"
            + "    \"event_ids\"\n"
            + "  ],\n"
            + "  \"count\": 1\n"
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
    @Test
    public void testStreamEvents() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{}"));
        Iterable<EventResult> response = client.events().streamEvents(
            StreamEventsRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());
        
        // Validate response deserialization
        Assertions.assertNotNull(response, "Response should not be null");
        // Verify the response can be serialized back to JSON
        String responseJson = objectMapper.writeValueAsString(response);
        Assertions.assertNotNull(responseJson);
        Assertions.assertFalse(responseJson.isEmpty());
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
