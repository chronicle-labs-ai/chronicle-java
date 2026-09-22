package com.chronicle.labs.api;

import com.chronicle.labs.api.ChronicleLabsApiClient;
import com.chronicle.labs.api.TestResources;
import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.agents.requests.CreateAgentChatSessionRequest;
import com.chronicle.labs.api.resources.agents.requests.GetAgentChatSessionRequest;
import com.chronicle.labs.api.resources.agents.requests.GetAgentSnapshotRequest;
import com.chronicle.labs.api.resources.agents.requests.PinLatestAgentVersionRequest;
import com.chronicle.labs.api.resources.agents.requests.RecordAgentRunsRequest;
import com.chronicle.labs.api.resources.agents.requests.RegisterAgentArtifactRequest;
import com.chronicle.labs.api.resources.agents.requests.SearchAgentHashIndexRequest;
import com.chronicle.labs.api.resources.agents.requests.SendAgentChatMessageRequest;
import com.chronicle.labs.api.resources.agents.requests.UpdateAgentRequest;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsRequestRunsItem;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsRequestRunsItemOperation;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsRequestRunsItemStatus;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsRequestRunsItemToolCallsItem;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsRequestRunsItemToolCallsItemStatus;
import com.chronicle.labs.api.resources.agents.types.RecordAgentRunsResponse;
import com.chronicle.labs.api.resources.agents.types.RegisterAgentArtifactRequestArtifact;
import com.chronicle.labs.api.resources.agents.types.RegisterAgentArtifactRequestArtifactFramework;
import com.chronicle.labs.api.resources.agents.types.RegisterAgentArtifactRequestArtifactModel;
import com.chronicle.labs.api.resources.agents.types.RegisterAgentArtifactRequestArtifactProvenance;
import com.chronicle.labs.api.resources.agents.types.RegisterAgentArtifactRequestArtifactToolsItem;
import com.chronicle.labs.api.types.AgentChatSession;
import com.chronicle.labs.api.types.AgentSnapshot;
import com.chronicle.labs.api.types.AgentSummary;
import com.chronicle.labs.api.types.AgentVersionSummary;
import com.chronicle.labs.api.types.CreateAgentChatSessionResponse;
import com.chronicle.labs.api.types.HashIndexEntry;
import com.chronicle.labs.api.types.SendAgentChatMessageResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.Iterable;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgentsWireTest {
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
    public void testListAgents() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("[{\"capabilityTags\":[\"capabilityTags\"],\"category\":\"category\",\"description\":\"description\",\"environment\":\"environment\",\"framework\":\"vercel-ai-sdk\",\"lastDriftAt\":\"2024-01-15T09:30:00Z\",\"lastRunAt\":\"2024-01-15T09:30:00Z\",\"latestVersion\":\"latestVersion\",\"model\":{\"label\":\"label\",\"modelId\":\"modelId\",\"provider\":\"provider\"},\"modelLabel\":\"modelLabel\",\"name\":\"name\",\"owner\":\"owner\",\"personaSummary\":\"personaSummary\",\"playgroundUrl\":\"playgroundUrl\",\"purpose\":\"purpose\",\"runbookUrl\":\"runbookUrl\",\"successRate\":1.1,\"totalRuns\":1,\"versionCount\":1}]"));
        List<AgentSummary> response = client.agents().listAgents();
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());
        
        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
            + "[\n"
            + "  {\n"
            + "    \"capabilityTags\": [\n"
            + "      \"capabilityTags\"\n"
            + "    ],\n"
            + "    \"category\": \"category\",\n"
            + "    \"description\": \"description\",\n"
            + "    \"environment\": \"environment\",\n"
            + "    \"framework\": \"vercel-ai-sdk\",\n"
            + "    \"lastDriftAt\": \"2024-01-15T09:30:00Z\",\n"
            + "    \"lastRunAt\": \"2024-01-15T09:30:00Z\",\n"
            + "    \"latestVersion\": \"latestVersion\",\n"
            + "    \"model\": {\n"
            + "      \"label\": \"label\",\n"
            + "      \"modelId\": \"modelId\",\n"
            + "      \"provider\": \"provider\"\n"
            + "    },\n"
            + "    \"modelLabel\": \"modelLabel\",\n"
            + "    \"name\": \"name\",\n"
            + "    \"owner\": \"owner\",\n"
            + "    \"personaSummary\": \"personaSummary\",\n"
            + "    \"playgroundUrl\": \"playgroundUrl\",\n"
            + "    \"purpose\": \"purpose\",\n"
            + "    \"runbookUrl\": \"runbookUrl\",\n"
            + "    \"successRate\": 1.1,\n"
            + "    \"totalRuns\": 1,\n"
            + "    \"versionCount\": 1\n"
            + "  }\n"
            + "]";
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
    public void testSearchAgentHashIndex() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("[{\"agentName\":\"agentName\",\"artifactId\":\"artifactId\",\"framework\":\"vercel-ai-sdk\",\"hash\":\"hash\",\"kind\":\"agent.root\",\"observedAt\":\"2024-01-15T09:30:00Z\",\"path\":\"path\",\"preview\":\"preview\",\"runId\":\"runId\"}]"));
        List<HashIndexEntry> response = client.agents().searchAgentHashIndex(
            SearchAgentHashIndexRequest
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
            + "[\n"
            + "  {\n"
            + "    \"agentName\": \"agentName\",\n"
            + "    \"artifactId\": \"artifactId\",\n"
            + "    \"framework\": \"vercel-ai-sdk\",\n"
            + "    \"hash\": \"hash\",\n"
            + "    \"kind\": \"agent.root\",\n"
            + "    \"observedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "    \"path\": \"path\",\n"
            + "    \"preview\": \"preview\",\n"
            + "    \"runId\": \"runId\"\n"
            + "  }\n"
            + "]";
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
    public void testSubscribeToAgentChanges() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{}"));
        Iterable<Map<String, Object>> response = client.agents().subscribeToAgentChanges();
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
    @Test
    public void testUpdateAgent() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"capabilityTags\":[\"capabilityTags\"],\"category\":\"category\",\"description\":\"description\",\"environment\":\"environment\",\"framework\":\"vercel-ai-sdk\",\"lastDriftAt\":\"2024-01-15T09:30:00Z\",\"lastRunAt\":\"2024-01-15T09:30:00Z\",\"latestVersion\":\"latestVersion\",\"model\":{\"label\":\"label\",\"modelId\":\"modelId\",\"provider\":\"provider\"},\"modelLabel\":\"modelLabel\",\"name\":\"name\",\"owner\":\"owner\",\"personaSummary\":\"personaSummary\",\"playgroundUrl\":\"playgroundUrl\",\"purpose\":\"purpose\",\"runbookUrl\":\"runbookUrl\",\"successRate\":1.1,\"totalRuns\":1,\"versionCount\":1}"));
        AgentSummary response = client.agents().updateAgent(
            "name",
            UpdateAgentRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{}";
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
            + "  \"capabilityTags\": [\n"
            + "    \"capabilityTags\"\n"
            + "  ],\n"
            + "  \"category\": \"category\",\n"
            + "  \"description\": \"description\",\n"
            + "  \"environment\": \"environment\",\n"
            + "  \"framework\": \"vercel-ai-sdk\",\n"
            + "  \"lastDriftAt\": \"2024-01-15T09:30:00Z\",\n"
            + "  \"lastRunAt\": \"2024-01-15T09:30:00Z\",\n"
            + "  \"latestVersion\": \"latestVersion\",\n"
            + "  \"model\": {\n"
            + "    \"label\": \"label\",\n"
            + "    \"modelId\": \"modelId\",\n"
            + "    \"provider\": \"provider\"\n"
            + "  },\n"
            + "  \"modelLabel\": \"modelLabel\",\n"
            + "  \"name\": \"name\",\n"
            + "  \"owner\": \"owner\",\n"
            + "  \"personaSummary\": \"personaSummary\",\n"
            + "  \"playgroundUrl\": \"playgroundUrl\",\n"
            + "  \"purpose\": \"purpose\",\n"
            + "  \"runbookUrl\": \"runbookUrl\",\n"
            + "  \"successRate\": 1.1,\n"
            + "  \"totalRuns\": 1,\n"
            + "  \"versionCount\": 1\n"
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
    public void testGetAgentSnapshot() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(TestResources.loadResource("/wire-tests/AgentsWireTest_testGetAgentSnapshot_response.json")));
        Optional<AgentSnapshot> response = client.agents().getAgentSnapshot(
            "name",
            GetAgentSnapshotRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());
        
        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = TestResources.loadResource("/wire-tests/AgentsWireTest_testGetAgentSnapshot_response.json");
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
    public void testPinLatestAgentVersion() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"capabilityTags\":[\"capabilityTags\"],\"category\":\"category\",\"description\":\"description\",\"environment\":\"environment\",\"framework\":\"vercel-ai-sdk\",\"lastDriftAt\":\"2024-01-15T09:30:00Z\",\"lastRunAt\":\"2024-01-15T09:30:00Z\",\"latestVersion\":\"latestVersion\",\"model\":{\"label\":\"label\",\"modelId\":\"modelId\",\"provider\":\"provider\"},\"modelLabel\":\"modelLabel\",\"name\":\"name\",\"owner\":\"owner\",\"personaSummary\":\"personaSummary\",\"playgroundUrl\":\"playgroundUrl\",\"purpose\":\"purpose\",\"runbookUrl\":\"runbookUrl\",\"successRate\":1.1,\"totalRuns\":1,\"versionCount\":1}"));
        AgentSummary response = client.agents().pinLatestAgentVersion(
            "name",
            PinLatestAgentVersionRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        
        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
            + "{\n"
            + "  \"capabilityTags\": [\n"
            + "    \"capabilityTags\"\n"
            + "  ],\n"
            + "  \"category\": \"category\",\n"
            + "  \"description\": \"description\",\n"
            + "  \"environment\": \"environment\",\n"
            + "  \"framework\": \"vercel-ai-sdk\",\n"
            + "  \"lastDriftAt\": \"2024-01-15T09:30:00Z\",\n"
            + "  \"lastRunAt\": \"2024-01-15T09:30:00Z\",\n"
            + "  \"latestVersion\": \"latestVersion\",\n"
            + "  \"model\": {\n"
            + "    \"label\": \"label\",\n"
            + "    \"modelId\": \"modelId\",\n"
            + "    \"provider\": \"provider\"\n"
            + "  },\n"
            + "  \"modelLabel\": \"modelLabel\",\n"
            + "  \"name\": \"name\",\n"
            + "  \"owner\": \"owner\",\n"
            + "  \"personaSummary\": \"personaSummary\",\n"
            + "  \"playgroundUrl\": \"playgroundUrl\",\n"
            + "  \"purpose\": \"purpose\",\n"
            + "  \"runbookUrl\": \"runbookUrl\",\n"
            + "  \"successRate\": 1.1,\n"
            + "  \"totalRuns\": 1,\n"
            + "  \"versionCount\": 1\n"
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
    public void testCreateAgentChatSession() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"session\":{\"agentName\":\"agentName\",\"agentVersion\":\"agentVersion\",\"artifactId\":\"artifactId\",\"messages\":[{\"messageId\":\"messageId\",\"occurredAt\":\"2024-01-15T09:30:00Z\",\"role\":\"user\",\"text\":\"text\"}],\"runId\":\"runId\",\"sessionId\":\"sessionId\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"traceId\":\"traceId\"}}"));
        CreateAgentChatSessionResponse response = client.agents().createAgentChatSession(
            "name",
            CreateAgentChatSessionRequest
                .builder()
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        
        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
            + "{\n"
            + "  \"session\": {\n"
            + "    \"agentName\": \"agentName\",\n"
            + "    \"agentVersion\": \"agentVersion\",\n"
            + "    \"artifactId\": \"artifactId\",\n"
            + "    \"messages\": [\n"
            + "      {\n"
            + "        \"messageId\": \"messageId\",\n"
            + "        \"occurredAt\": \"2024-01-15T09:30:00Z\",\n"
            + "        \"role\": \"user\",\n"
            + "        \"text\": \"text\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"runId\": \"runId\",\n"
            + "    \"sessionId\": \"sessionId\",\n"
            + "    \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "    \"traceId\": \"traceId\"\n"
            + "  }\n"
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
    public void testGetAgentChatSession() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"agentName\":\"agentName\",\"agentVersion\":\"agentVersion\",\"artifactId\":\"artifactId\",\"messages\":[{\"error\":\"error\",\"eventIds\":[\"eventIds\"],\"messageId\":\"messageId\",\"occurredAt\":\"2024-01-15T09:30:00Z\",\"role\":\"user\",\"steps\":[{\"eventType\":\"eventType\",\"source\":\"source\",\"stepId\":\"stepId\",\"text\":\"text\",\"toolName\":\"toolName\"}],\"text\":\"text\"}],\"runId\":\"runId\",\"sessionId\":\"sessionId\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"traceId\":\"traceId\"}"));
        AgentChatSession response = client.agents().getAgentChatSession(
            "name",
            "session_id",
            GetAgentChatSessionRequest
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
            + "  \"agentName\": \"agentName\",\n"
            + "  \"agentVersion\": \"agentVersion\",\n"
            + "  \"artifactId\": \"artifactId\",\n"
            + "  \"messages\": [\n"
            + "    {\n"
            + "      \"error\": \"error\",\n"
            + "      \"eventIds\": [\n"
            + "        \"eventIds\"\n"
            + "      ],\n"
            + "      \"messageId\": \"messageId\",\n"
            + "      \"occurredAt\": \"2024-01-15T09:30:00Z\",\n"
            + "      \"role\": \"user\",\n"
            + "      \"steps\": [\n"
            + "        {\n"
            + "          \"eventType\": \"eventType\",\n"
            + "          \"source\": \"source\",\n"
            + "          \"stepId\": \"stepId\",\n"
            + "          \"text\": \"text\",\n"
            + "          \"toolName\": \"toolName\"\n"
            + "        }\n"
            + "      ],\n"
            + "      \"text\": \"text\"\n"
            + "    }\n"
            + "  ],\n"
            + "  \"runId\": \"runId\",\n"
            + "  \"sessionId\": \"sessionId\",\n"
            + "  \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "  \"traceId\": \"traceId\"\n"
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
    public void testSendAgentChatMessage() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"session\":{\"agentName\":\"agentName\",\"agentVersion\":\"agentVersion\",\"artifactId\":\"artifactId\",\"messages\":[{\"messageId\":\"messageId\",\"occurredAt\":\"2024-01-15T09:30:00Z\",\"role\":\"user\",\"text\":\"text\"}],\"runId\":\"runId\",\"sessionId\":\"sessionId\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"traceId\":\"traceId\"}}"));
        SendAgentChatMessageResponse response = client.agents().sendAgentChatMessage(
            "name",
            "session_id",
            SendAgentChatMessageRequest
                .builder()
                .text("text")
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{\n"
            + "  \"text\": \"text\"\n"
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
            + "  \"session\": {\n"
            + "    \"agentName\": \"agentName\",\n"
            + "    \"agentVersion\": \"agentVersion\",\n"
            + "    \"artifactId\": \"artifactId\",\n"
            + "    \"messages\": [\n"
            + "      {\n"
            + "        \"messageId\": \"messageId\",\n"
            + "        \"occurredAt\": \"2024-01-15T09:30:00Z\",\n"
            + "        \"role\": \"user\",\n"
            + "        \"text\": \"text\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"runId\": \"runId\",\n"
            + "    \"sessionId\": \"sessionId\",\n"
            + "    \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "    \"traceId\": \"traceId\"\n"
            + "  }\n"
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
    public void testRegisterAgentArtifact() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody(TestResources.loadResource("/wire-tests/AgentsWireTest_testRegisterAgentArtifact_response.json")));
        AgentVersionSummary response = client.agents().registerAgentArtifact(
            RegisterAgentArtifactRequest
                .builder()
                .artifact(
                    RegisterAgentArtifactRequestArtifact
                        .builder()
                        .artifactId("artifactId")
                        .configHash("configHash")
                        .framework(RegisterAgentArtifactRequestArtifactFramework.VERCEL_AI_SDK)
                        .model(
                            RegisterAgentArtifactRequestArtifactModel
                                .builder()
                                .label("label")
                                .build()
                        )
                        .name("name")
                        .provenance(
                            RegisterAgentArtifactRequestArtifactProvenance
                                .builder()
                                .createdAt(OffsetDateTime.parse("2024-01-15T09:30:00Z"))
                                .build()
                        )
                        .schemaVersion("schemaVersion")
                        .version("version")
                        .tools(
                            Arrays.asList(
                                RegisterAgentArtifactRequestArtifactToolsItem
                                    .builder()
                                    .name("name")
                                    .build()
                            )
                        )
                        .build()
                )
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{\n"
            + "  \"artifact\": {\n"
            + "    \"artifactId\": \"artifactId\",\n"
            + "    \"configHash\": \"configHash\",\n"
            + "    \"framework\": \"vercel-ai-sdk\",\n"
            + "    \"model\": {\n"
            + "      \"label\": \"label\"\n"
            + "    },\n"
            + "    \"name\": \"name\",\n"
            + "    \"provenance\": {\n"
            + "      \"createdAt\": \"2024-01-15T09:30:00Z\"\n"
            + "    },\n"
            + "    \"schemaVersion\": \"schemaVersion\",\n"
            + "    \"tools\": [\n"
            + "      {\n"
            + "        \"name\": \"name\"\n"
            + "      }\n"
            + "    ],\n"
            + "    \"version\": \"version\"\n"
            + "  }\n"
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
        String expectedResponseBody = TestResources.loadResource("/wire-tests/AgentsWireTest_testRegisterAgentArtifact_response.json");
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
    public void testRecordAgentRuns() throws Exception {
        server.enqueue(new MockResponse()
            .setResponseCode(200)
            .setBody("{\"accepted\":1}"));
        RecordAgentRunsResponse response = client.agents().recordAgentRuns(
            RecordAgentRunsRequest
                .builder()
                .runs(
                    Arrays.asList(
                        RecordAgentRunsRequestRunsItem
                            .builder()
                            .artifactId("artifactId")
                            .configHash("configHash")
                            .operation(RecordAgentRunsRequestRunsItemOperation.GENERATE)
                            .runId("runId")
                            .schemaVersion("schemaVersion")
                            .startedAt(OffsetDateTime.parse("2024-01-15T09:30:00Z"))
                            .status(RecordAgentRunsRequestRunsItemStatus.STARTED)
                            .toolCalls(
                                Arrays.asList(
                                    RecordAgentRunsRequestRunsItemToolCallsItem
                                        .builder()
                                        .callId("callId")
                                        .startedAt(OffsetDateTime.parse("2024-01-15T09:30:00Z"))
                                        .status(RecordAgentRunsRequestRunsItemToolCallsItemStatus.STARTED)
                                        .toolName("toolName")
                                        .build()
                                )
                            )
                            .build()
                    )
                )
                .build()
        );
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
            + "{\n"
            + "  \"runs\": [\n"
            + "    {\n"
            + "      \"artifactId\": \"artifactId\",\n"
            + "      \"configHash\": \"configHash\",\n"
            + "      \"operation\": \"generate\",\n"
            + "      \"runId\": \"runId\",\n"
            + "      \"schemaVersion\": \"schemaVersion\",\n"
            + "      \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "      \"status\": \"started\",\n"
            + "      \"toolCalls\": [\n"
            + "        {\n"
            + "          \"callId\": \"callId\",\n"
            + "          \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
            + "          \"status\": \"started\",\n"
            + "          \"toolName\": \"toolName\"\n"
            + "        }\n"
            + "      ]\n"
            + "    }\n"
            + "  ]\n"
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
            + "  \"accepted\": 1\n"
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
