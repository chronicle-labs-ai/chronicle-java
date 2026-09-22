package com.chronicle.labs.api;

import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.environments.requests.CompileEnvironmentRequest;
import com.chronicle.labs.api.resources.environments.requests.CreateEnvironmentRequest;
import com.chronicle.labs.api.resources.environments.requests.CreateEnvironmentVersionRequest;
import com.chronicle.labs.api.resources.environments.requests.GetEnvironmentRequest;
import com.chronicle.labs.api.resources.environments.requests.GetEnvironmentVersionRequest;
import com.chronicle.labs.api.resources.environments.requests.ListEnvironmentVersionsRequest;
import com.chronicle.labs.api.types.CompileEnvironmentResponse;
import com.chronicle.labs.api.types.EnvironmentResponse;
import com.chronicle.labs.api.types.EnvironmentVersionRecord;
import com.chronicle.labs.api.types.EnvironmentVersionResponse;
import com.chronicle.labs.api.types.ListEnvironmentsResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnvironmentsWireTest {
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
    public void testListEnvironments() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environments\":[{\"environment\":{\"createdAt\":\"2024-01-15T09:30:00Z\",\"id\":\"id\",\"label\":\"label\",\"slug\":\"slug\",\"tenantId\":\"tenantId\"},\"versions\":[{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{}},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}]}]}"));
        ListEnvironmentsResponse response = client.environments().listEnvironments();
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"environments\": [\n"
                + "    {\n"
                + "      \"environment\": {\n"
                + "        \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "        \"id\": \"id\",\n"
                + "        \"label\": \"label\",\n"
                + "        \"slug\": \"slug\",\n"
                + "        \"tenantId\": \"tenantId\"\n"
                + "      },\n"
                + "      \"versions\": [\n"
                + "        {\n"
                + "          \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "          \"environmentId\": \"environmentId\",\n"
                + "          \"id\": \"id\",\n"
                + "          \"spec\": {\n"
                + "            \"interception\": {}\n"
                + "          },\n"
                + "          \"status\": \"draft\",\n"
                + "          \"tenantId\": \"tenantId\",\n"
                + "          \"version\": \"version\"\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testCreateEnvironment() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environment\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdAt\":\"2024-01-15T09:30:00Z\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"slug\":\"slug\",\"tenantId\":\"tenantId\"},\"versions\":[{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{}},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}]}"));
        EnvironmentResponse response = client.environments()
                .createEnvironment(CreateEnvironmentRequest.builder()
                        .slug("slug")
                        .label("label")
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"slug\": \"slug\",\n" + "  \"label\": \"label\"\n" + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type"))
                discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind"))
                discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualJson.isNull()) {
            Assertions.assertTrue(
                    actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(),
                    "request should be a valid JSON value");
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
                + "  \"environment\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"label\": \"label\",\n"
                + "    \"slug\": \"slug\",\n"
                + "    \"tenantId\": \"tenantId\"\n"
                + "  },\n"
                + "  \"versions\": [\n"
                + "    {\n"
                + "      \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"environmentId\": \"environmentId\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"spec\": {\n"
                + "        \"interception\": {}\n"
                + "      },\n"
                + "      \"status\": \"draft\",\n"
                + "      \"tenantId\": \"tenantId\",\n"
                + "      \"version\": \"version\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testGetEnvironment() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environment\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdAt\":\"2024-01-15T09:30:00Z\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"slug\":\"slug\",\"tenantId\":\"tenantId\"},\"versions\":[{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{}},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}]}"));
        EnvironmentResponse response = client.environments()
                .getEnvironment(
                        "environment_id", GetEnvironmentRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"environment\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"label\": \"label\",\n"
                + "    \"slug\": \"slug\",\n"
                + "    \"tenantId\": \"tenantId\"\n"
                + "  },\n"
                + "  \"versions\": [\n"
                + "    {\n"
                + "      \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"environmentId\": \"environmentId\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"spec\": {\n"
                + "        \"interception\": {}\n"
                + "      },\n"
                + "      \"status\": \"draft\",\n"
                + "      \"tenantId\": \"tenantId\",\n"
                + "      \"version\": \"version\"\n"
                + "    }\n"
                + "  ]\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testListEnvironmentVersions() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{},\"mcp\":[{\"name\":\"name\"}],\"services\":[{\"name\":\"name\"}],\"twins\":[{\"service\":\"service\"}]},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}]"));
        List<EnvironmentVersionRecord> response = client.environments()
                .listEnvironmentVersions(
                        "environment_id",
                        ListEnvironmentVersionsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"environmentId\": \"environmentId\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"spec\": {\n"
                + "      \"interception\": {},\n"
                + "      \"mcp\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"services\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"twins\": [\n"
                + "        {\n"
                + "          \"service\": \"service\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"status\": \"draft\",\n"
                + "    \"tenantId\": \"tenantId\",\n"
                + "    \"version\": \"version\"\n"
                + "  }\n"
                + "]";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testCreateEnvironmentVersion() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environment\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdAt\":\"2024-01-15T09:30:00Z\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"slug\":\"slug\",\"tenantId\":\"tenantId\"},\"version\":{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{},\"mcp\":[{\"name\":\"name\"}],\"services\":[{\"name\":\"name\"}],\"twins\":[{\"service\":\"service\"}]},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}}"));
        EnvironmentVersionResponse response = client.environments()
                .createEnvironmentVersion(
                        "environment_id",
                        CreateEnvironmentVersionRequest.builder()
                                .version("version")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"version\": \"version\"\n" + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type"))
                discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind"))
                discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualJson.isNull()) {
            Assertions.assertTrue(
                    actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(),
                    "request should be a valid JSON value");
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
                + "  \"environment\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"label\": \"label\",\n"
                + "    \"slug\": \"slug\",\n"
                + "    \"tenantId\": \"tenantId\"\n"
                + "  },\n"
                + "  \"version\": {\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"environmentId\": \"environmentId\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"spec\": {\n"
                + "      \"interception\": {},\n"
                + "      \"mcp\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"services\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"twins\": [\n"
                + "        {\n"
                + "          \"service\": \"service\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"status\": \"draft\",\n"
                + "    \"tenantId\": \"tenantId\",\n"
                + "    \"version\": \"version\"\n"
                + "  }\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testGetEnvironmentVersion() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environment\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdAt\":\"2024-01-15T09:30:00Z\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"slug\":\"slug\",\"tenantId\":\"tenantId\"},\"version\":{\"createdAt\":\"2024-01-15T09:30:00Z\",\"environmentId\":\"environmentId\",\"id\":\"id\",\"spec\":{\"interception\":{},\"mcp\":[{\"name\":\"name\"}],\"services\":[{\"name\":\"name\"}],\"twins\":[{\"service\":\"service\"}]},\"status\":\"draft\",\"tenantId\":\"tenantId\",\"version\":\"version\"}}"));
        EnvironmentVersionResponse response = client.environments()
                .getEnvironmentVersion(
                        "environment_id",
                        "version_selector",
                        GetEnvironmentVersionRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"environment\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"label\": \"label\",\n"
                + "    \"slug\": \"slug\",\n"
                + "    \"tenantId\": \"tenantId\"\n"
                + "  },\n"
                + "  \"version\": {\n"
                + "    \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"environmentId\": \"environmentId\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"spec\": {\n"
                + "      \"interception\": {},\n"
                + "      \"mcp\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"services\": [\n"
                + "        {\n"
                + "          \"name\": \"name\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"twins\": [\n"
                + "        {\n"
                + "          \"service\": \"service\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"status\": \"draft\",\n"
                + "    \"tenantId\": \"tenantId\",\n"
                + "    \"version\": \"version\"\n"
                + "  }\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
        }

        if (actualResponseNode.isArray()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Array should have valid size");
        }
        if (actualResponseNode.isObject()) {
            Assertions.assertTrue(actualResponseNode.size() >= 0, "Object should have valid field count");
        }
    }

    @Test
    public void testCompileEnvironmentVersion() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"environmentId\":\"environmentId\",\"environmentSlug\":\"environmentSlug\",\"versionId\":\"versionId\",\"version\":\"version\",\"tenantId\":\"tenantId\",\"datasetSnapshotId\":\"datasetSnapshotId\",\"scenarioId\":\"scenarioId\",\"bundleId\":\"bundleId\",\"sha256\":\"sha256\",\"uri\":\"uri\",\"packageUri\":\"packageUri\",\"rootDir\":\"rootDir\",\"sizeBytes\":1000000,\"warnings\":[\"warnings\"],\"files\":[\"files\"],\"manifest\":{\"key\":\"value\"}}"));
        CompileEnvironmentResponse response = client.environments()
                .compileEnvironmentVersion(
                        "environment_id",
                        "version_selector",
                        CompileEnvironmentRequest.builder()
                                .datasetSnapshotId("datasetSnapshotId")
                                .scenarioId("scenarioId")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"datasetSnapshotId\": \"datasetSnapshotId\",\n"
                + "  \"scenarioId\": \"scenarioId\"\n"
                + "}";
        JsonNode actualJson = objectMapper.readTree(actualRequestBody);
        JsonNode expectedJson = objectMapper.readTree(expectedRequestBody);
        Assertions.assertTrue(jsonEquals(expectedJson, actualJson), "Request body structure does not match expected");
        if (actualJson.has("type") || actualJson.has("_type") || actualJson.has("kind")) {
            String discriminator = null;
            if (actualJson.has("type")) discriminator = actualJson.get("type").asText();
            else if (actualJson.has("_type"))
                discriminator = actualJson.get("_type").asText();
            else if (actualJson.has("kind"))
                discriminator = actualJson.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualJson.isNull()) {
            Assertions.assertTrue(
                    actualJson.isObject() || actualJson.isArray() || actualJson.isValueNode(),
                    "request should be a valid JSON value");
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
                + "  \"environmentId\": \"environmentId\",\n"
                + "  \"environmentSlug\": \"environmentSlug\",\n"
                + "  \"versionId\": \"versionId\",\n"
                + "  \"version\": \"version\",\n"
                + "  \"tenantId\": \"tenantId\",\n"
                + "  \"datasetSnapshotId\": \"datasetSnapshotId\",\n"
                + "  \"scenarioId\": \"scenarioId\",\n"
                + "  \"bundleId\": \"bundleId\",\n"
                + "  \"sha256\": \"sha256\",\n"
                + "  \"uri\": \"uri\",\n"
                + "  \"packageUri\": \"packageUri\",\n"
                + "  \"rootDir\": \"rootDir\",\n"
                + "  \"sizeBytes\": 1000000,\n"
                + "  \"warnings\": [\n"
                + "    \"warnings\"\n"
                + "  ],\n"
                + "  \"files\": [\n"
                + "    \"files\"\n"
                + "  ],\n"
                + "  \"manifest\": {\n"
                + "    \"key\": \"value\"\n"
                + "  }\n"
                + "}";
        JsonNode actualResponseNode = objectMapper.readTree(actualResponseJson);
        JsonNode expectedResponseNode = objectMapper.readTree(expectedResponseBody);
        Assertions.assertTrue(
                jsonEquals(expectedResponseNode, actualResponseNode),
                "Response body structure does not match expected");
        if (actualResponseNode.has("type") || actualResponseNode.has("_type") || actualResponseNode.has("kind")) {
            String discriminator = null;
            if (actualResponseNode.has("type"))
                discriminator = actualResponseNode.get("type").asText();
            else if (actualResponseNode.has("_type"))
                discriminator = actualResponseNode.get("_type").asText();
            else if (actualResponseNode.has("kind"))
                discriminator = actualResponseNode.get("kind").asText();
            Assertions.assertNotNull(discriminator, "Union type should have a discriminator field");
            Assertions.assertFalse(discriminator.isEmpty(), "Union discriminator should not be empty");
        }

        if (!actualResponseNode.isNull()) {
            Assertions.assertTrue(
                    actualResponseNode.isObject() || actualResponseNode.isArray() || actualResponseNode.isValueNode(),
                    "response should be a valid JSON value");
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
        if (expected.isNumber() && actual.isNumber())
            return Math.abs(expected.doubleValue() - actual.doubleValue()) < 1e-10;
        if (expected.isObject() && actual.isObject()) {
            java.util.Iterator<java.util.Map.Entry<String, JsonNode>> iter = expected.fields();
            while (iter.hasNext()) {
                java.util.Map.Entry<String, JsonNode> entry = iter.next();
                JsonNode actualValue = actual.get(entry.getKey());
                if (actualValue == null) {
                    if (!entry.getValue().isNull()) return false;
                } else if (!jsonEquals(entry.getValue(), actualValue)) return false;
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
