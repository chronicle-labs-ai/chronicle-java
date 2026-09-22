package com.chronicle.labs.api;

import com.chronicle.labs.api.core.ObjectMappers;
import com.chronicle.labs.api.resources.datasets.requests.AddTaskFromTraceRequest;
import com.chronicle.labs.api.resources.datasets.requests.ArchiveDatasetRequest;
import com.chronicle.labs.api.resources.datasets.requests.CreateClusterRequest;
import com.chronicle.labs.api.resources.datasets.requests.CreateDatasetTaskRequest;
import com.chronicle.labs.api.resources.datasets.requests.CreateSavedViewRequest;
import com.chronicle.labs.api.resources.datasets.requests.CreateTaskSuitePayload;
import com.chronicle.labs.api.resources.datasets.requests.CreateTaskSuiteWithTraceRequest;
import com.chronicle.labs.api.resources.datasets.requests.DatasetSavedViewPatch;
import com.chronicle.labs.api.resources.datasets.requests.DeleteDatasetClusterRequest;
import com.chronicle.labs.api.resources.datasets.requests.DeleteDatasetSavedViewRequest;
import com.chronicle.labs.api.resources.datasets.requests.DeleteDatasetTaskRequest;
import com.chronicle.labs.api.resources.datasets.requests.GetDatasetRequest;
import com.chronicle.labs.api.resources.datasets.requests.GetDatasetSnapshotRequest;
import com.chronicle.labs.api.resources.datasets.requests.GetDatasetTaskRequest;
import com.chronicle.labs.api.resources.datasets.requests.GetDatasetVersionRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetClustersRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetEvaluationRunsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetSavedViewsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetTaskEventsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetTasksRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetTraceEventsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetTracesRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetVersionsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListDatasetsRequest;
import com.chronicle.labs.api.resources.datasets.requests.ListTraceDatasetMembershipsRequest;
import com.chronicle.labs.api.resources.datasets.requests.PublishVersionRequest;
import com.chronicle.labs.api.resources.datasets.requests.RefreshDatasetTaskRequest;
import com.chronicle.labs.api.resources.datasets.requests.RefreshDatasetTraceRequest;
import com.chronicle.labs.api.resources.datasets.requests.RemoveTraceFromDatasetRequest;
import com.chronicle.labs.api.resources.datasets.requests.SetTaskVerifiersRequest;
import com.chronicle.labs.api.resources.datasets.requests.TaskSuitePatch;
import com.chronicle.labs.api.resources.datasets.requests.UpdateClusterRequest;
import com.chronicle.labs.api.resources.datasets.requests.UpdateDatasetTaskRequest;
import com.chronicle.labs.api.resources.datasets.requests.UpdateTracesRequest;
import com.chronicle.labs.api.resources.datasets.types.CreateSavedViewRequestScope;
import com.chronicle.labs.api.resources.datasets.types.CreateSavedViewRequestState;
import com.chronicle.labs.api.resources.datasets.types.CreateTaskSuiteWithTraceRequestDataset;
import com.chronicle.labs.api.resources.datasets.types.CreateTaskSuiteWithTraceRequestTrace;
import com.chronicle.labs.api.resources.datasets.types.SetTaskVerifiersRequestVerifiersItem;
import com.chronicle.labs.api.resources.datasets.types.UpdateTracesRequestPatch;
import com.chronicle.labs.api.types.AddTaskFromTraceResponse;
import com.chronicle.labs.api.types.CreateTaskRequest;
import com.chronicle.labs.api.types.CreateTaskSuiteWithTraceResponse;
import com.chronicle.labs.api.types.DatasetCluster;
import com.chronicle.labs.api.types.DatasetSavedView;
import com.chronicle.labs.api.types.RefreshMembershipRequest;
import com.chronicle.labs.api.types.Task;
import com.chronicle.labs.api.types.TaskEventPage;
import com.chronicle.labs.api.types.TaskMembership;
import com.chronicle.labs.api.types.TaskPage;
import com.chronicle.labs.api.types.TaskSuite;
import com.chronicle.labs.api.types.TaskSuiteDetail;
import com.chronicle.labs.api.types.TaskSuiteEvalRun;
import com.chronicle.labs.api.types.TaskSuitePage;
import com.chronicle.labs.api.types.TaskSuiteSnapshot;
import com.chronicle.labs.api.types.TaskSuiteVersion;
import com.chronicle.labs.api.types.UpdateTaskRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DatasetsWireTest {
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
    public void testListDatasets() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"items\":[{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"}],\"nextCursor\":\"nextCursor\"}"));
        TaskSuitePage response =
                client.datasets().listDatasets(ListDatasetsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"createdBy\": \"createdBy\",\n"
                + "      \"description\": \"description\",\n"
                + "      \"eventCount\": 1,\n"
                + "      \"id\": \"id\",\n"
                + "      \"name\": \"name\",\n"
                + "      \"purpose\": \"eval\",\n"
                + "      \"tags\": [\n"
                + "        \"tags\"\n"
                + "      ],\n"
                + "      \"traceCount\": 1,\n"
                + "      \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nextCursor\": \"nextCursor\"\n"
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
    public void testCreateDataset() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"}"));
        TaskSuite response = client.datasets()
                .createDataset(CreateTaskSuitePayload.builder().name("name").build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"name\": \"name\"\n" + "}";
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
                + "  \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"createdBy\": \"createdBy\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"eventCount\": 1,\n"
                + "  \"id\": \"id\",\n"
                + "  \"name\": \"name\",\n"
                + "  \"purpose\": \"eval\",\n"
                + "  \"tags\": [\n"
                + "    \"tags\"\n"
                + "  ],\n"
                + "  \"traceCount\": 1,\n"
                + "  \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testCreateDatasetWithTrace() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"dataset\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"},\"membership\":{\"addedAt\":\"addedAt\",\"datasetId\":\"datasetId\",\"datasetName\":\"datasetName\",\"eventCount\":1,\"id\":\"id\",\"note\":\"note\",\"purpose\":\"eval\",\"refreshAvailable\":true,\"revision\":1,\"split\":\"train\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\"}}"));
        CreateTaskSuiteWithTraceResponse response = client.datasets()
                .createDatasetWithTrace(CreateTaskSuiteWithTraceRequest.builder()
                        .dataset(CreateTaskSuiteWithTraceRequestDataset.builder()
                                .name("name")
                                .build())
                        .trace(CreateTaskSuiteWithTraceRequestTrace.builder()
                                .traceId("traceId")
                                .build())
                        .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"dataset\": {\n"
                + "    \"name\": \"name\"\n"
                + "  },\n"
                + "  \"trace\": {\n"
                + "    \"traceId\": \"traceId\"\n"
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
                + "  \"dataset\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdBy\": \"createdBy\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"tags\": [\n"
                + "      \"tags\"\n"
                + "    ],\n"
                + "    \"traceCount\": 1,\n"
                + "    \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
                + "  },\n"
                + "  \"membership\": {\n"
                + "    \"addedAt\": \"addedAt\",\n"
                + "    \"datasetId\": \"datasetId\",\n"
                + "    \"datasetName\": \"datasetName\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"note\": \"note\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"refreshAvailable\": true,\n"
                + "    \"revision\": 1,\n"
                + "    \"split\": \"train\",\n"
                + "    \"subjectId\": \"subjectId\",\n"
                + "    \"subjectKind\": \"trace\",\n"
                + "    \"traceId\": \"traceId\"\n"
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
    public void testGetDataset() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"clusters\":[{\"color\":\"color\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"similarityCenter\":[1.1],\"traceIds\":[\"traceIds\"]}],\"dataset\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"},\"edges\":[{\"fromTraceId\":\"fromTraceId\",\"toTraceId\":\"toTraceId\",\"weight\":1.1}]}"));
        TaskSuiteDetail response = client.datasets()
                .getDataset("dataset_id", GetDatasetRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"clusters\": [\n"
                + "    {\n"
                + "      \"color\": \"color\",\n"
                + "      \"description\": \"description\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"label\": \"label\",\n"
                + "      \"similarityCenter\": [\n"
                + "        1.1\n"
                + "      ],\n"
                + "      \"traceIds\": [\n"
                + "        \"traceIds\"\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"dataset\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdBy\": \"createdBy\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"tags\": [\n"
                + "      \"tags\"\n"
                + "    ],\n"
                + "    \"traceCount\": 1,\n"
                + "    \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
                + "  },\n"
                + "  \"edges\": [\n"
                + "    {\n"
                + "      \"fromTraceId\": \"fromTraceId\",\n"
                + "      \"toTraceId\": \"toTraceId\",\n"
                + "      \"weight\": 1.1\n"
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
    public void testArchiveDataset() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .archiveDataset("dataset_id", ArchiveDatasetRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testUpdateDataset() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"}"));
        TaskSuite response = client.datasets()
                .updateDataset("dataset_id", TaskSuitePatch.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"createdBy\": \"createdBy\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"eventCount\": 1,\n"
                + "  \"id\": \"id\",\n"
                + "  \"name\": \"name\",\n"
                + "  \"purpose\": \"eval\",\n"
                + "  \"tags\": [\n"
                + "    \"tags\"\n"
                + "  ],\n"
                + "  \"traceCount\": 1,\n"
                + "  \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testGetDatasetSnapshot() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/DatasetsWireTest_testGetDatasetSnapshot_response.json")));
        TaskSuiteSnapshot response = client.datasets()
                .getDatasetSnapshot(
                        "dataset_id", GetDatasetSnapshotRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/DatasetsWireTest_testGetDatasetSnapshot_response.json");
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
    public void testListDatasetTraces() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"items\":[{\"addedAt\":\"2024-01-15T09:30:00Z\",\"addedBy\":\"addedBy\",\"clusterId\":\"clusterId\",\"durationMs\":1,\"embedding\":[1.1],\"eventCount\":1,\"label\":\"label\",\"membershipId\":\"membershipId\",\"note\":\"note\",\"primarySource\":\"primarySource\",\"refreshAvailable\":true,\"revision\":1,\"sources\":[\"sources\"],\"split\":\"train\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"status\":\"ok\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\",\"verifiers\":[{\"scorerId\":\"scorerId\"}]}],\"nextCursor\":\"nextCursor\"}"));
        TaskPage response = client.datasets()
                .listDatasetTraces(
                        "dataset_id", ListDatasetTracesRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"addedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"addedBy\": \"addedBy\",\n"
                + "      \"clusterId\": \"clusterId\",\n"
                + "      \"durationMs\": 1,\n"
                + "      \"embedding\": [\n"
                + "        1.1\n"
                + "      ],\n"
                + "      \"eventCount\": 1,\n"
                + "      \"label\": \"label\",\n"
                + "      \"membershipId\": \"membershipId\",\n"
                + "      \"note\": \"note\",\n"
                + "      \"primarySource\": \"primarySource\",\n"
                + "      \"refreshAvailable\": true,\n"
                + "      \"revision\": 1,\n"
                + "      \"sources\": [\n"
                + "        \"sources\"\n"
                + "      ],\n"
                + "      \"split\": \"train\",\n"
                + "      \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"status\": \"ok\",\n"
                + "      \"subjectId\": \"subjectId\",\n"
                + "      \"subjectKind\": \"trace\",\n"
                + "      \"traceId\": \"traceId\",\n"
                + "      \"verifiers\": [\n"
                + "        {\n"
                + "          \"scorerId\": \"scorerId\"\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nextCursor\": \"nextCursor\"\n"
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
    public void testAddTraceToDataset() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"dataset\":{\"archivedAt\":\"2024-01-15T09:30:00Z\",\"createdBy\":\"createdBy\",\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"name\":\"name\",\"purpose\":\"eval\",\"tags\":[\"tags\"],\"traceCount\":1,\"updatedAt\":\"2024-01-15T09:30:00Z\"},\"membership\":{\"addedAt\":\"addedAt\",\"datasetId\":\"datasetId\",\"datasetName\":\"datasetName\",\"eventCount\":1,\"id\":\"id\",\"note\":\"note\",\"purpose\":\"eval\",\"refreshAvailable\":true,\"revision\":1,\"split\":\"train\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\"}}"));
        AddTaskFromTraceResponse response = client.datasets()
                .addTraceToDataset(
                        "dataset_id",
                        AddTaskFromTraceRequest.builder().traceId("traceId").build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"traceId\": \"traceId\"\n" + "}";
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
                + "  \"dataset\": {\n"
                + "    \"archivedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"createdBy\": \"createdBy\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"tags\": [\n"
                + "      \"tags\"\n"
                + "    ],\n"
                + "    \"traceCount\": 1,\n"
                + "    \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
                + "  },\n"
                + "  \"membership\": {\n"
                + "    \"addedAt\": \"addedAt\",\n"
                + "    \"datasetId\": \"datasetId\",\n"
                + "    \"datasetName\": \"datasetName\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"note\": \"note\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"refreshAvailable\": true,\n"
                + "    \"revision\": 1,\n"
                + "    \"split\": \"train\",\n"
                + "    \"subjectId\": \"subjectId\",\n"
                + "    \"subjectKind\": \"trace\",\n"
                + "    \"traceId\": \"traceId\"\n"
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
    public void testUpdateDatasetTraces() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .updateDatasetTraces(
                        "dataset_id",
                        UpdateTracesRequest.builder()
                                .patch(UpdateTracesRequestPatch.builder().build())
                                .traceIds(Arrays.asList("traceIds"))
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody =
                "" + "{\n" + "  \"patch\": {},\n" + "  \"traceIds\": [\n" + "    \"traceIds\"\n" + "  ]\n" + "}";
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
    }

    @Test
    public void testRemoveTraceFromDataset() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .removeTraceFromDataset(
                        "dataset_id",
                        "membership_id",
                        RemoveTraceFromDatasetRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testRefreshDatasetTrace() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"addedAt\":\"addedAt\",\"datasetId\":\"datasetId\",\"datasetName\":\"datasetName\",\"eventCount\":1,\"id\":\"id\",\"note\":\"note\",\"purpose\":\"eval\",\"refreshAvailable\":true,\"revision\":1,\"split\":\"train\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\"}"));
        TaskMembership response = client.datasets()
                .refreshDatasetTrace(
                        "dataset_id",
                        "membership_id",
                        RefreshDatasetTraceRequest.builder()
                                .body(RefreshMembershipRequest.builder().build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"addedAt\": \"addedAt\",\n"
                + "  \"datasetId\": \"datasetId\",\n"
                + "  \"datasetName\": \"datasetName\",\n"
                + "  \"eventCount\": 1,\n"
                + "  \"id\": \"id\",\n"
                + "  \"note\": \"note\",\n"
                + "  \"purpose\": \"eval\",\n"
                + "  \"refreshAvailable\": true,\n"
                + "  \"revision\": 1,\n"
                + "  \"split\": \"train\",\n"
                + "  \"subjectId\": \"subjectId\",\n"
                + "  \"subjectKind\": \"trace\",\n"
                + "  \"traceId\": \"traceId\"\n"
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
    public void testListDatasetTraceEvents() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"items\":[{\"actor\":\"actor\",\"color\":\"color\",\"correlationKey\":\"correlationKey\",\"id\":\"id\",\"message\":\"message\",\"occurredAt\":\"2024-01-15T09:30:00Z\",\"parentEventId\":\"parentEventId\",\"payload\":{\"key\":\"value\"},\"source\":\"source\",\"stream\":\"stream\",\"traceId\":\"traceId\",\"traceLabel\":\"traceLabel\",\"type\":\"type\"}],\"nextCursor\":\"nextCursor\"}"));
        TaskEventPage response = client.datasets()
                .listDatasetTraceEvents(
                        "dataset_id",
                        "membership_id",
                        ListDatasetTraceEventsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"actor\": \"actor\",\n"
                + "      \"color\": \"color\",\n"
                + "      \"correlationKey\": \"correlationKey\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"message\": \"message\",\n"
                + "      \"occurredAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"parentEventId\": \"parentEventId\",\n"
                + "      \"payload\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"source\": \"source\",\n"
                + "      \"stream\": \"stream\",\n"
                + "      \"traceId\": \"traceId\",\n"
                + "      \"traceLabel\": \"traceLabel\",\n"
                + "      \"type\": \"type\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nextCursor\": \"nextCursor\"\n"
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
    public void testListTraceDatasetMemberships() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"addedAt\":\"addedAt\",\"datasetId\":\"datasetId\",\"datasetName\":\"datasetName\",\"eventCount\":1,\"id\":\"id\",\"note\":\"note\",\"purpose\":\"eval\",\"refreshAvailable\":true,\"revision\":1,\"split\":\"train\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\"}]"));
        List<TaskMembership> response = client.datasets()
                .listTraceDatasetMemberships(
                        "trace_id", ListTraceDatasetMembershipsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"addedAt\": \"addedAt\",\n"
                + "    \"datasetId\": \"datasetId\",\n"
                + "    \"datasetName\": \"datasetName\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"note\": \"note\",\n"
                + "    \"purpose\": \"eval\",\n"
                + "    \"refreshAvailable\": true,\n"
                + "    \"revision\": 1,\n"
                + "    \"split\": \"train\",\n"
                + "    \"subjectId\": \"subjectId\",\n"
                + "    \"subjectKind\": \"trace\",\n"
                + "    \"traceId\": \"traceId\"\n"
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
    public void testListDatasetTasks() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"items\":[{\"addedAt\":\"2024-01-15T09:30:00Z\",\"addedBy\":\"addedBy\",\"clusterId\":\"clusterId\",\"durationMs\":1,\"embedding\":[1.1],\"eventCount\":1,\"label\":\"label\",\"membershipId\":\"membershipId\",\"note\":\"note\",\"primarySource\":\"primarySource\",\"refreshAvailable\":true,\"revision\":1,\"sources\":[\"sources\"],\"split\":\"train\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"status\":\"ok\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\",\"verifiers\":[{\"scorerId\":\"scorerId\"}]}],\"nextCursor\":\"nextCursor\"}"));
        TaskPage response = client.datasets()
                .listDatasetTasks(
                        "dataset_id", ListDatasetTasksRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"addedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"addedBy\": \"addedBy\",\n"
                + "      \"clusterId\": \"clusterId\",\n"
                + "      \"durationMs\": 1,\n"
                + "      \"embedding\": [\n"
                + "        1.1\n"
                + "      ],\n"
                + "      \"eventCount\": 1,\n"
                + "      \"label\": \"label\",\n"
                + "      \"membershipId\": \"membershipId\",\n"
                + "      \"note\": \"note\",\n"
                + "      \"primarySource\": \"primarySource\",\n"
                + "      \"refreshAvailable\": true,\n"
                + "      \"revision\": 1,\n"
                + "      \"sources\": [\n"
                + "        \"sources\"\n"
                + "      ],\n"
                + "      \"split\": \"train\",\n"
                + "      \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"status\": \"ok\",\n"
                + "      \"subjectId\": \"subjectId\",\n"
                + "      \"subjectKind\": \"trace\",\n"
                + "      \"traceId\": \"traceId\",\n"
                + "      \"verifiers\": [\n"
                + "        {\n"
                + "          \"scorerId\": \"scorerId\"\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nextCursor\": \"nextCursor\"\n"
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
    public void testCreateDatasetTask() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/DatasetsWireTest_testCreateDatasetTask_response.json")));
        Task response = client.datasets()
                .createDatasetTask(
                        "dataset_id",
                        CreateDatasetTaskRequest.builder()
                                .body(CreateTaskRequest.of(new HashMap<String, Object>() {
                                    {
                                        put("key", "value");
                                    }
                                }))
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"key\": \"value\"\n" + "}";
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/DatasetsWireTest_testCreateDatasetTask_response.json");
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
    public void testGetDatasetTask() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"addedAt\":\"2024-01-15T09:30:00Z\",\"addedBy\":\"addedBy\",\"clusterId\":\"clusterId\",\"durationMs\":1,\"embedding\":[1.1],\"eventCount\":1,\"label\":\"label\",\"membershipId\":\"membershipId\",\"note\":\"note\",\"primarySource\":\"primarySource\",\"refreshAvailable\":true,\"revision\":1,\"sources\":[\"sources\"],\"split\":\"train\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"status\":\"ok\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"task\":{\"config\":{\"agentTimeoutSec\":1.1,\"networkMode\":\"public\",\"verifierTimeoutSec\":1.1},\"environmentId\":\"environmentId\",\"environmentVersionId\":\"environmentVersionId\",\"expectedOutcome\":{\"key\":\"value\"},\"instruction\":\"instruction\",\"seedCutoffEventId\":\"seedCutoffEventId\",\"solution\":\"solution\",\"title\":\"title\"},\"traceId\":\"traceId\",\"verifiers\":[{\"passThreshold\":1.1,\"scorerId\":\"scorerId\",\"weight\":\"low\"}]}"));
        Task response = client.datasets()
                .getDatasetTask(
                        "dataset_id",
                        "membership_id",
                        GetDatasetTaskRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"addedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"addedBy\": \"addedBy\",\n"
                + "  \"clusterId\": \"clusterId\",\n"
                + "  \"durationMs\": 1,\n"
                + "  \"embedding\": [\n"
                + "    1.1\n"
                + "  ],\n"
                + "  \"eventCount\": 1,\n"
                + "  \"label\": \"label\",\n"
                + "  \"membershipId\": \"membershipId\",\n"
                + "  \"note\": \"note\",\n"
                + "  \"primarySource\": \"primarySource\",\n"
                + "  \"refreshAvailable\": true,\n"
                + "  \"revision\": 1,\n"
                + "  \"sources\": [\n"
                + "    \"sources\"\n"
                + "  ],\n"
                + "  \"split\": \"train\",\n"
                + "  \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"status\": \"ok\",\n"
                + "  \"subjectId\": \"subjectId\",\n"
                + "  \"subjectKind\": \"trace\",\n"
                + "  \"task\": {\n"
                + "    \"config\": {\n"
                + "      \"agentTimeoutSec\": 1.1,\n"
                + "      \"networkMode\": \"public\",\n"
                + "      \"verifierTimeoutSec\": 1.1\n"
                + "    },\n"
                + "    \"environmentId\": \"environmentId\",\n"
                + "    \"environmentVersionId\": \"environmentVersionId\",\n"
                + "    \"expectedOutcome\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"instruction\": \"instruction\",\n"
                + "    \"seedCutoffEventId\": \"seedCutoffEventId\",\n"
                + "    \"solution\": \"solution\",\n"
                + "    \"title\": \"title\"\n"
                + "  },\n"
                + "  \"traceId\": \"traceId\",\n"
                + "  \"verifiers\": [\n"
                + "    {\n"
                + "      \"passThreshold\": 1.1,\n"
                + "      \"scorerId\": \"scorerId\",\n"
                + "      \"weight\": \"low\"\n"
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
    public void testDeleteDatasetTask() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .deleteDatasetTask(
                        "dataset_id",
                        "membership_id",
                        DeleteDatasetTaskRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testUpdateDatasetTask() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/DatasetsWireTest_testUpdateDatasetTask_response.json")));
        Task response = client.datasets()
                .updateDatasetTask(
                        "dataset_id",
                        "membership_id",
                        UpdateDatasetTaskRequest.builder()
                                .body(UpdateTaskRequest.of(new HashMap<String, Object>() {
                                    {
                                        put("key", "value");
                                    }
                                }))
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"key\": \"value\"\n" + "}";
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
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/DatasetsWireTest_testUpdateDatasetTask_response.json");
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
    public void testSetDatasetTaskVerifiers() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"addedAt\":\"2024-01-15T09:30:00Z\",\"addedBy\":\"addedBy\",\"clusterId\":\"clusterId\",\"durationMs\":1,\"embedding\":[1.1],\"eventCount\":1,\"label\":\"label\",\"membershipId\":\"membershipId\",\"note\":\"note\",\"primarySource\":\"primarySource\",\"refreshAvailable\":true,\"revision\":1,\"sources\":[\"sources\"],\"split\":\"train\",\"startedAt\":\"2024-01-15T09:30:00Z\",\"status\":\"ok\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"task\":{\"config\":{\"agentTimeoutSec\":1.1,\"networkMode\":\"public\",\"verifierTimeoutSec\":1.1},\"environmentId\":\"environmentId\",\"environmentVersionId\":\"environmentVersionId\",\"expectedOutcome\":{\"key\":\"value\"},\"instruction\":\"instruction\",\"seedCutoffEventId\":\"seedCutoffEventId\",\"solution\":\"solution\",\"title\":\"title\"},\"traceId\":\"traceId\",\"verifiers\":[{\"passThreshold\":1.1,\"scorerId\":\"scorerId\",\"weight\":\"low\"}]}"));
        Task response = client.datasets()
                .setDatasetTaskVerifiers(
                        "dataset_id",
                        "membership_id",
                        SetTaskVerifiersRequest.builder()
                                .verifiers(Arrays.asList(SetTaskVerifiersRequestVerifiersItem.builder()
                                        .scorerId("scorerId")
                                        .build()))
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PUT", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = ""
                + "{\n"
                + "  \"verifiers\": [\n"
                + "    {\n"
                + "      \"scorerId\": \"scorerId\"\n"
                + "    }\n"
                + "  ]\n"
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
                + "  \"addedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"addedBy\": \"addedBy\",\n"
                + "  \"clusterId\": \"clusterId\",\n"
                + "  \"durationMs\": 1,\n"
                + "  \"embedding\": [\n"
                + "    1.1\n"
                + "  ],\n"
                + "  \"eventCount\": 1,\n"
                + "  \"label\": \"label\",\n"
                + "  \"membershipId\": \"membershipId\",\n"
                + "  \"note\": \"note\",\n"
                + "  \"primarySource\": \"primarySource\",\n"
                + "  \"refreshAvailable\": true,\n"
                + "  \"revision\": 1,\n"
                + "  \"sources\": [\n"
                + "    \"sources\"\n"
                + "  ],\n"
                + "  \"split\": \"train\",\n"
                + "  \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "  \"status\": \"ok\",\n"
                + "  \"subjectId\": \"subjectId\",\n"
                + "  \"subjectKind\": \"trace\",\n"
                + "  \"task\": {\n"
                + "    \"config\": {\n"
                + "      \"agentTimeoutSec\": 1.1,\n"
                + "      \"networkMode\": \"public\",\n"
                + "      \"verifierTimeoutSec\": 1.1\n"
                + "    },\n"
                + "    \"environmentId\": \"environmentId\",\n"
                + "    \"environmentVersionId\": \"environmentVersionId\",\n"
                + "    \"expectedOutcome\": {\n"
                + "      \"key\": \"value\"\n"
                + "    },\n"
                + "    \"instruction\": \"instruction\",\n"
                + "    \"seedCutoffEventId\": \"seedCutoffEventId\",\n"
                + "    \"solution\": \"solution\",\n"
                + "    \"title\": \"title\"\n"
                + "  },\n"
                + "  \"traceId\": \"traceId\",\n"
                + "  \"verifiers\": [\n"
                + "    {\n"
                + "      \"passThreshold\": 1.1,\n"
                + "      \"scorerId\": \"scorerId\",\n"
                + "      \"weight\": \"low\"\n"
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
    public void testListDatasetTaskEvents() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"hasMore\":true,\"items\":[{\"actor\":\"actor\",\"color\":\"color\",\"correlationKey\":\"correlationKey\",\"id\":\"id\",\"message\":\"message\",\"occurredAt\":\"2024-01-15T09:30:00Z\",\"parentEventId\":\"parentEventId\",\"payload\":{\"key\":\"value\"},\"source\":\"source\",\"stream\":\"stream\",\"traceId\":\"traceId\",\"traceLabel\":\"traceLabel\",\"type\":\"type\"}],\"nextCursor\":\"nextCursor\"}"));
        TaskEventPage response = client.datasets()
                .listDatasetTaskEvents(
                        "dataset_id",
                        "membership_id",
                        ListDatasetTaskEventsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "{\n"
                + "  \"hasMore\": true,\n"
                + "  \"items\": [\n"
                + "    {\n"
                + "      \"actor\": \"actor\",\n"
                + "      \"color\": \"color\",\n"
                + "      \"correlationKey\": \"correlationKey\",\n"
                + "      \"id\": \"id\",\n"
                + "      \"message\": \"message\",\n"
                + "      \"occurredAt\": \"2024-01-15T09:30:00Z\",\n"
                + "      \"parentEventId\": \"parentEventId\",\n"
                + "      \"payload\": {\n"
                + "        \"key\": \"value\"\n"
                + "      },\n"
                + "      \"source\": \"source\",\n"
                + "      \"stream\": \"stream\",\n"
                + "      \"traceId\": \"traceId\",\n"
                + "      \"traceLabel\": \"traceLabel\",\n"
                + "      \"type\": \"type\"\n"
                + "    }\n"
                + "  ],\n"
                + "  \"nextCursor\": \"nextCursor\"\n"
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
    public void testRefreshDatasetTask() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"addedAt\":\"addedAt\",\"datasetId\":\"datasetId\",\"datasetName\":\"datasetName\",\"eventCount\":1,\"id\":\"id\",\"note\":\"note\",\"purpose\":\"eval\",\"refreshAvailable\":true,\"revision\":1,\"split\":\"train\",\"subjectId\":\"subjectId\",\"subjectKind\":\"trace\",\"traceId\":\"traceId\"}"));
        TaskMembership response = client.datasets()
                .refreshDatasetTask(
                        "dataset_id",
                        "membership_id",
                        RefreshDatasetTaskRequest.builder()
                                .body(RefreshMembershipRequest.builder().build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"addedAt\": \"addedAt\",\n"
                + "  \"datasetId\": \"datasetId\",\n"
                + "  \"datasetName\": \"datasetName\",\n"
                + "  \"eventCount\": 1,\n"
                + "  \"id\": \"id\",\n"
                + "  \"note\": \"note\",\n"
                + "  \"purpose\": \"eval\",\n"
                + "  \"refreshAvailable\": true,\n"
                + "  \"revision\": 1,\n"
                + "  \"split\": \"train\",\n"
                + "  \"subjectId\": \"subjectId\",\n"
                + "  \"subjectKind\": \"trace\",\n"
                + "  \"traceId\": \"traceId\"\n"
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
    public void testListDatasetClusters() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"color\":\"color\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"similarityCenter\":[1.1],\"traceIds\":[\"traceIds\"]}]"));
        List<DatasetCluster> response = client.datasets()
                .listDatasetClusters(
                        "dataset_id", ListDatasetClustersRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"color\": \"color\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"label\": \"label\",\n"
                + "    \"similarityCenter\": [\n"
                + "      1.1\n"
                + "    ],\n"
                + "    \"traceIds\": [\n"
                + "      \"traceIds\"\n"
                + "    ]\n"
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
    public void testCreateDatasetCluster() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"color\":\"color\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"similarityCenter\":[1.1],\"traceIds\":[\"traceIds\"]}"));
        DatasetCluster response = client.datasets()
                .createDatasetCluster(
                        "dataset_id",
                        CreateClusterRequest.builder()
                                .color("color")
                                .label("label")
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{\n" + "  \"color\": \"color\",\n" + "  \"label\": \"label\"\n" + "}";
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
                + "  \"color\": \"color\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"id\": \"id\",\n"
                + "  \"label\": \"label\",\n"
                + "  \"similarityCenter\": [\n"
                + "    1.1\n"
                + "  ],\n"
                + "  \"traceIds\": [\n"
                + "    \"traceIds\"\n"
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
    public void testDeleteDatasetCluster() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .deleteDatasetCluster(
                        "dataset_id",
                        "cluster_id",
                        DeleteDatasetClusterRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testUpdateDatasetCluster() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"color\":\"color\",\"description\":\"description\",\"id\":\"id\",\"label\":\"label\",\"similarityCenter\":[1.1],\"traceIds\":[\"traceIds\"]}"));
        DatasetCluster response = client.datasets()
                .updateDatasetCluster(
                        "dataset_id",
                        "cluster_id",
                        UpdateClusterRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"color\": \"color\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"id\": \"id\",\n"
                + "  \"label\": \"label\",\n"
                + "  \"similarityCenter\": [\n"
                + "    1.1\n"
                + "  ],\n"
                + "  \"traceIds\": [\n"
                + "    \"traceIds\"\n"
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
    public void testListDatasetSavedViews() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"createdBy\":\"createdBy\",\"description\":\"description\",\"id\":\"id\",\"name\":\"name\",\"scope\":\"personal\",\"shortcut\":\"shortcut\",\"state\":{\"density\":\"density\",\"displayProperties\":[\"displayProperties\"],\"groupBy\":\"groupBy\",\"lens\":\"lens\",\"ordering\":\"ordering\",\"search\":\"search\",\"showEmptyGroups\":true,\"sorting\":[{\"desc\":true,\"id\":\"id\"}]},\"updatedAt\":\"2024-01-15T09:30:00Z\"}]"));
        List<DatasetSavedView> response = client.datasets()
                .listDatasetSavedViews(
                        "dataset_id", ListDatasetSavedViewsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"createdBy\": \"createdBy\",\n"
                + "    \"description\": \"description\",\n"
                + "    \"id\": \"id\",\n"
                + "    \"name\": \"name\",\n"
                + "    \"scope\": \"personal\",\n"
                + "    \"shortcut\": \"shortcut\",\n"
                + "    \"state\": {\n"
                + "      \"density\": \"density\",\n"
                + "      \"displayProperties\": [\n"
                + "        \"displayProperties\"\n"
                + "      ],\n"
                + "      \"groupBy\": \"groupBy\",\n"
                + "      \"lens\": \"lens\",\n"
                + "      \"ordering\": \"ordering\",\n"
                + "      \"search\": \"search\",\n"
                + "      \"showEmptyGroups\": true,\n"
                + "      \"sorting\": [\n"
                + "        {\n"
                + "          \"desc\": true,\n"
                + "          \"id\": \"id\"\n"
                + "        }\n"
                + "      ]\n"
                + "    },\n"
                + "    \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testCreateDatasetSavedView() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"createdBy\":\"createdBy\",\"description\":\"description\",\"id\":\"id\",\"name\":\"name\",\"scope\":\"personal\",\"shortcut\":\"shortcut\",\"state\":{\"density\":\"density\",\"displayProperties\":[\"displayProperties\"],\"groupBy\":\"groupBy\",\"lens\":\"lens\",\"ordering\":\"ordering\",\"search\":\"search\",\"showEmptyGroups\":true,\"sorting\":[{\"desc\":true,\"id\":\"id\"}]},\"updatedAt\":\"2024-01-15T09:30:00Z\"}"));
        DatasetSavedView response = client.datasets()
                .createDatasetSavedView(
                        "dataset_id",
                        CreateSavedViewRequest.builder()
                                .name("name")
                                .scope(CreateSavedViewRequestScope.PERSONAL)
                                .state(CreateSavedViewRequestState.builder().build())
                                .build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody =
                "" + "{\n" + "  \"name\": \"name\",\n" + "  \"scope\": \"personal\",\n" + "  \"state\": {}\n" + "}";
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
                + "  \"createdBy\": \"createdBy\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"id\": \"id\",\n"
                + "  \"name\": \"name\",\n"
                + "  \"scope\": \"personal\",\n"
                + "  \"shortcut\": \"shortcut\",\n"
                + "  \"state\": {\n"
                + "    \"density\": \"density\",\n"
                + "    \"displayProperties\": [\n"
                + "      \"displayProperties\"\n"
                + "    ],\n"
                + "    \"groupBy\": \"groupBy\",\n"
                + "    \"lens\": \"lens\",\n"
                + "    \"ordering\": \"ordering\",\n"
                + "    \"search\": \"search\",\n"
                + "    \"showEmptyGroups\": true,\n"
                + "    \"sorting\": [\n"
                + "      {\n"
                + "        \"desc\": true,\n"
                + "        \"id\": \"id\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testDeleteDatasetSavedView() throws Exception {
        server.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        client.datasets()
                .deleteDatasetSavedView(
                        "dataset_id",
                        "view_id",
                        DeleteDatasetSavedViewRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("DELETE", request.getMethod());
    }

    @Test
    public void testUpdateDatasetSavedView() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"createdBy\":\"createdBy\",\"description\":\"description\",\"id\":\"id\",\"name\":\"name\",\"scope\":\"personal\",\"shortcut\":\"shortcut\",\"state\":{\"density\":\"density\",\"displayProperties\":[\"displayProperties\"],\"groupBy\":\"groupBy\",\"lens\":\"lens\",\"ordering\":\"ordering\",\"search\":\"search\",\"showEmptyGroups\":true,\"sorting\":[{\"desc\":true,\"id\":\"id\"}]},\"updatedAt\":\"2024-01-15T09:30:00Z\"}"));
        DatasetSavedView response = client.datasets()
                .updateDatasetSavedView(
                        "dataset_id", "view_id", DatasetSavedViewPatch.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("PATCH", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"createdBy\": \"createdBy\",\n"
                + "  \"description\": \"description\",\n"
                + "  \"id\": \"id\",\n"
                + "  \"name\": \"name\",\n"
                + "  \"scope\": \"personal\",\n"
                + "  \"shortcut\": \"shortcut\",\n"
                + "  \"state\": {\n"
                + "    \"density\": \"density\",\n"
                + "    \"displayProperties\": [\n"
                + "      \"displayProperties\"\n"
                + "    ],\n"
                + "    \"groupBy\": \"groupBy\",\n"
                + "    \"lens\": \"lens\",\n"
                + "    \"ordering\": \"ordering\",\n"
                + "    \"search\": \"search\",\n"
                + "    \"showEmptyGroups\": true,\n"
                + "    \"sorting\": [\n"
                + "      {\n"
                + "        \"desc\": true,\n"
                + "        \"id\": \"id\"\n"
                + "      }\n"
                + "    ]\n"
                + "  },\n"
                + "  \"updatedAt\": \"2024-01-15T09:30:00Z\"\n"
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
    public void testListDatasetVersions() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"createdAt\":\"createdAt\",\"createdBy\":\"createdBy\",\"datasetId\":\"datasetId\",\"datasetRevision\":1000000,\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"itemCount\":1,\"label\":\"label\",\"version\":1}]"));
        List<TaskSuiteVersion> response = client.datasets()
                .listDatasetVersions(
                        "dataset_id", ListDatasetVersionsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"createdAt\": \"createdAt\",\n"
                + "    \"createdBy\": \"createdBy\",\n"
                + "    \"datasetId\": \"datasetId\",\n"
                + "    \"datasetRevision\": 1000000,\n"
                + "    \"description\": \"description\",\n"
                + "    \"eventCount\": 1,\n"
                + "    \"id\": \"id\",\n"
                + "    \"itemCount\": 1,\n"
                + "    \"label\": \"label\",\n"
                + "    \"version\": 1\n"
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
    public void testPublishDatasetVersion() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "{\"createdAt\":\"createdAt\",\"createdBy\":\"createdBy\",\"datasetId\":\"datasetId\",\"datasetRevision\":1000000,\"description\":\"description\",\"eventCount\":1,\"id\":\"id\",\"itemCount\":1,\"label\":\"label\",\"version\":1}"));
        TaskSuiteVersion response = client.datasets()
                .publishDatasetVersion(
                        "dataset_id", PublishVersionRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("POST", request.getMethod());
        // Validate request body
        String actualRequestBody = request.getBody().readUtf8();
        String expectedRequestBody = "" + "{}";
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
                + "  \"createdAt\": \"createdAt\",\n"
                + "  \"createdBy\": \"createdBy\",\n"
                + "  \"datasetId\": \"datasetId\",\n"
                + "  \"datasetRevision\": 1000000,\n"
                + "  \"description\": \"description\",\n"
                + "  \"eventCount\": 1,\n"
                + "  \"id\": \"id\",\n"
                + "  \"itemCount\": 1,\n"
                + "  \"label\": \"label\",\n"
                + "  \"version\": 1\n"
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
    public void testGetDatasetVersion() throws Exception {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(TestResources.loadResource(
                        "/wire-tests/DatasetsWireTest_testGetDatasetVersion_response.json")));
        TaskSuiteSnapshot response = client.datasets()
                .getDatasetVersion(
                        "dataset_id",
                        "version_id",
                        GetDatasetVersionRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody =
                TestResources.loadResource("/wire-tests/DatasetsWireTest_testGetDatasetVersion_response.json");
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
    public void testListDatasetEvaluationRuns() throws Exception {
        server.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setBody(
                                "[{\"agentLabel\":\"agentLabel\",\"failedTraceIds\":[\"failedTraceIds\"],\"id\":\"id\",\"note\":\"note\",\"passRate\":1.1,\"startedAt\":\"2024-01-15T09:30:00Z\",\"status\":\"passing\",\"taskResults\":[{\"passed\":true,\"traceId\":\"traceId\"}],\"totalCount\":1}]"));
        List<TaskSuiteEvalRun> response = client.datasets()
                .listDatasetEvaluationRuns(
                        "dataset_id", ListDatasetEvaluationRunsRequest.builder().build());
        RecordedRequest request = server.takeRequest();
        Assertions.assertNotNull(request);
        Assertions.assertEquals("GET", request.getMethod());

        // Validate response body
        Assertions.assertNotNull(response, "Response should not be null");
        String actualResponseJson = objectMapper.writeValueAsString(response);
        String expectedResponseBody = ""
                + "[\n"
                + "  {\n"
                + "    \"agentLabel\": \"agentLabel\",\n"
                + "    \"failedTraceIds\": [\n"
                + "      \"failedTraceIds\"\n"
                + "    ],\n"
                + "    \"id\": \"id\",\n"
                + "    \"note\": \"note\",\n"
                + "    \"passRate\": 1.1,\n"
                + "    \"startedAt\": \"2024-01-15T09:30:00Z\",\n"
                + "    \"status\": \"passing\",\n"
                + "    \"taskResults\": [\n"
                + "      {\n"
                + "        \"passed\": true,\n"
                + "        \"traceId\": \"traceId\"\n"
                + "      }\n"
                + "    ],\n"
                + "    \"totalCount\": 1\n"
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
