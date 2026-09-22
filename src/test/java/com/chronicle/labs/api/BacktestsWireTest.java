package com.chronicle.labs.api;

import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.backtests.requests.CancelBacktestJobRequest;
import com.chronicle.labs.api.resources.backtests.requests.CreateBacktestJobRequest;
import com.chronicle.labs.api.resources.backtests.requests.GetBacktestJobRequest;
import com.chronicle.labs.api.resources.backtests.requests.GetBacktestTrialRequest;
import com.chronicle.labs.api.resources.backtests.requests.ListBacktestJobTrialsRequest;
import com.chronicle.labs.api.resources.backtests.requests.ListBacktestJobsRequest;
import com.chronicle.labs.api.resources.backtests.requests.StreamBacktestJobEventsRequest;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipe;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeAgentsItem;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeData;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeDataKind;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeDataScenariosItem;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeDataScenariosItemKind;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeDataSourcesItem;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeDataSourcesItemKind;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeGradersItem;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeGradersItemKind;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeGradersItemSource;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeGradersItemWeight;
import com.chronicle.labs.api.resources.backtests.types.CreateBacktestJobRequestRecipeMode;
import com.chronicle.labs.api.types.BacktestJobDetailResponse;
import com.chronicle.labs.api.types.BacktestTrialDetailResponse;
import com.chronicle.labs.api.types.BacktestsAvailability;
import com.chronicle.labs.api.types.CancelBacktestJobResponse;
import com.chronicle.labs.api.types.CreateBacktestJobResponse;
import com.chronicle.labs.api.types.ListBacktestJobTrialsResponse;
import com.chronicle.labs.api.types.ListBacktestJobsResponse;
import com.chronicle.labs.api.types.TrialEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BacktestsWireTest {
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
    public void testGetBacktestsAvailability() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/BacktestsWireTest_testGetBacktestsAvailability_response.json")));
        BacktestsAvailability response = client.backtests().getBacktestsAvailability();
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/BacktestsWireTest_testGetBacktestsAvailability_response.json");
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
    public void testListBacktestJobs() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"nextOffset\":1,\"runs\":[{\"agentIds\":[\"agentIds\"],\"datasetLabel\":\"datasetLabel\",\"divergences\":1,\"environmentLabel\":\"environmentLabel\",\"hue\":\"hue\",\"id\":\"id\",\"mode\":\"replay\",\"name\":\"name\",\"owner\":\"owner\",\"scheduledFor\":\"2024-01-15T09:30:00Z\",\"status\":\"running\",\"totalRuns\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\",\"verdict\":\"verdict\"}]}"));
        ListBacktestJobsResponse response = client.backtests()
                .listBacktestJobs(ListBacktestJobsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"nextOffset\": 1,\n"
                + "  \"runs\": [\n"
                + "    {\n"
                + "      \"agentIds\": [\n"
                + "        \"agentIds\"\n"
                + "      ],\n"
                + "      \"datasetLabel\": \"datasetLabel\",\n"
                + "      \"divergences\": 1,\n"
                + "      \"environmentLabel\": \"environmentLabel\",\n"
                + "      \"hue\": \"hue\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"mode\": \"replay\",\n"
                + "      \"name\": \"name\",\n"
                + "      \"owner\": \"owner\",\n"
                + "      \"scheduledFor\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"status\": \"running\",\n"
                + "      \"totalRuns\": 1,\n"
                + "      \"updatedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"verdict\": \"verdict\"\n"
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
    public void testCreateBacktestJob() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"jobId\":\"jobId\",\"run\":{\"agentIds\":[\"agentIds\"],\"datasetLabel\":\"datasetLabel\",\"divergences\":1,\"environmentLabel\":\"environmentLabel\",\"hue\":\"hue\",\"id\":\"id\",\"mode\":\"replay\",\"name\":\"name\",\"owner\":\"owner\",\"scheduledFor\":\"2024-01-15T09:30:00Z\",\"status\":\"running\",\"totalRuns\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\",\"verdict\":\"verdict\"}}"));
        CreateBacktestJobResponse response = client.backtests()
                .createBacktestJob(CreateBacktestJobRequest.builder()
                        .name("name")
                        .recipe(CreateBacktestJobRequestRecipe.builder()
                                .data(CreateBacktestJobRequestRecipeData.builder()
                                        .kind(CreateBacktestJobRequestRecipeDataKind.COMPOSED)
                                        .scenarios(
                                                Arrays.asList(CreateBacktestJobRequestRecipeDataScenariosItem.builder()
                                                        .count(1)
                                                        .id("id")
                                                        .kind(
                                                                CreateBacktestJobRequestRecipeDataScenariosItemKind
                                                                        .ADVERSARIAL)
                                                        .label("label")
                                                        .build()))
                                        .sources(Arrays.asList(CreateBacktestJobRequestRecipeDataSourcesItem.builder()
                                                .count(1)
                                                .id("id")
                                                .kind(CreateBacktestJobRequestRecipeDataSourcesItemKind.PROD)
                                                .label("label")
                                                .build()))
                                        .build())
                                .mode(CreateBacktestJobRequestRecipeMode.REPLAY)
                                .name("name")
                                .agents(Arrays.asList(CreateBacktestJobRequestRecipeAgentsItem.builder()
                                        .hue("hue")
                                        .id("id")
                                        .label("label")
                                        .notes("notes")
                                        .build()))
                                .graders(Arrays.asList(CreateBacktestJobRequestRecipeGradersItem.builder()
                                        .id("id")
                                        .kind(CreateBacktestJobRequestRecipeGradersItemKind.RUBRIC)
                                        .label("label")
                                        .source(CreateBacktestJobRequestRecipeGradersItemSource.PROPOSED)
                                        .weight(CreateBacktestJobRequestRecipeGradersItemWeight.LOW)
                                        .build()))
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"name\": \"name\",\n"
                + "  \"recipe\": {\n"
                + "    \"agents\": [\n"
                + "      {\n"
                + "        \"hue\": \"hue\",\n"
                + "        \"id\": \"id\",\n"
                + "        \"label\": \"label\",\n"
                + "        \"notes\": \"notes\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"data\": {\n"
                + "      \"kind\": \"composed\",\n"
                + "      \"scenarios\": [\n"
                + "        {\n"
                + "          \"count\": 1,\n"
                + "          \"id\": \"id\",\n"
                + "          \"kind\": \"adversarial\",\n"
                + "          \"label\": \"label\"\n"
                + "        }\n"
                + "      ],\n"
                + "      \"sources\": [\n"
                + "        {\n"
                + "          \"count\": 1,\n"
                + "          \"id\": \"id\",\n"
                + "          \"kind\": \"prod\",\n"
                + "          \"label\": \"label\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"graders\": [\n"
                + "      {\n"
                + "        \"id\": \"id\",\n"
                + "        \"kind\": \"rubric\",\n"
                + "        \"label\": \"label\",\n"
                + "        \"source\": \"proposed\",\n"
                + "        \"weight\": \"low\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"mode\": \"replay\",\n"
                + "    \"name\": \"name\"\n"
                + "  }\n"
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
                + "  \"jobId\": \"jobId\",\n"
                + "  \"run\": {\n"
                + "    \"agentIds\": [\n"
                + "      \"agentIds\"\n"
                + "    ],\n"
                + "    \"datasetLabel\": \"datasetLabel\",\n"
                + "    \"divergences\": 1,\n"
                + "    \"environmentLabel\": \"environmentLabel\",\n"
                + "    \"hue\": \"hue\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"mode\": \"replay\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"owner\": \"owner\",\n"
                + "    \"scheduledFor\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"status\": \"running\",\n"
                + "    \"totalRuns\": 1,\n"
                + "    \"updatedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"verdict\": \"verdict\"\n"
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
    public void testGetBacktestJob() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource("/wire-tests/BacktestsWireTest_testGetBacktestJob_response.json")));
        BacktestJobDetailResponse response = client.backtests()
                .getBacktestJob("job_id", GetBacktestJobRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/BacktestsWireTest_testGetBacktestJob_response.json");
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
    public void testListBacktestJobTrials() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"nextOffset\":1,\"rewards\":{\"key\":{\"key\":1.1}},\"trials\":[{\"agentId\":\"agentId\",\"agentLabel\":\"agentLabel\",\"attempt\":1,\"caseCluster\":\"caseCluster\",\"caseId\":\"caseId\",\"createdAt\":\"2024-01-15T09:30:00Z\",\"durationMs\":1,\"exception\":{\"kind\":\"kind\",\"message\":\"message\"},\"id\":\"id\",\"instruction\":\"instruction\",\"isBaseline\":true,\"jobId\":\"jobId\",\"sandboxId\":\"sandboxId\",\"status\":\"pending\",\"tenantId\":\"tenantId\",\"timings\":{},\"updatedAt\":\"2024-01-15T09:30:00Z\"}]}"));
        ListBacktestJobTrialsResponse response = client.backtests()
                .listBacktestJobTrials(
                        "job_id", ListBacktestJobTrialsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"nextOffset\": 1,\n"
                + "  \"rewards\": {\n"
                + "    \"key\": {\n"
                + "      \"key\": 1.1\n"
                + "    }\n"
                + "  },\n"
                + "  \"trials\": [\n"
                + "    {\n"
                + "      \"agentId\": \"agentId\",\n"
                + "      \"agentLabel\": \"agentLabel\",\n"
                + "      \"attempt\": 1,\n"
                + "      \"caseCluster\": \"caseCluster\",\n"
                + "      \"caseId\": \"caseId\",\n"
                + "      \"createdAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"durationMs\": 1,\n"
                + "      \"exception\": {\n"
                + "        \"kind\": \"kind\",\n"
                + "        \"message\": \"message\"\n"
                + "      },\n"
                + "      \"id\": \"id\",\n"
                + "      \"instruction\": \"instruction\",\n"
                + "      \"isBaseline\": true,\n"
                + "      \"jobId\": \"jobId\",\n"
                + "      \"sandboxId\": \"sandboxId\",\n"
                + "      \"status\": \"pending\",\n"
                + "      \"tenantId\": \"tenantId\",\n"
                + "      \"timings\": {},\n"
                + "      \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testGetBacktestTrial() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/BacktestsWireTest_testGetBacktestTrial_response.json")));
        BacktestTrialDetailResponse response = client.backtests()
                .getBacktestTrial(
                        "job_id", "trial_id", GetBacktestTrialRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/BacktestsWireTest_testGetBacktestTrial_response.json");
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
    public void testCancelBacktestJob() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"aborted\":true,\"jobId\":\"jobId\",\"previousStatus\":\"pending\"}"));
        CancelBacktestJobResponse response = client.backtests()
                .cancelBacktestJob("job_id", CancelBacktestJobRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"aborted\": true,\n"
                + "  \"jobId\": \"jobId\",\n"
                + "  \"previousStatus\": \"pending\"\n"
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
    public void testStreamBacktestJobEvents() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        Iterable<TrialEvent> response = client.backtests()
                .streamBacktestJobEvents(
                        "job_id", StreamBacktestJobEventsRequest.builder().build());
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
