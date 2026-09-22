# Reference
## events
<details><summary><code>client.events.queryEvents() -> EventListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. Results are scoped to the tenant of the API key and ordered newest first by event time and event ID. Pass the opaque `next_cursor` as `cursor` to continue without an offset scan.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().queryEvents(
    QueryEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**source:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**topic:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**eventType:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityType:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityId:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Page size. Values above 200 are clamped to 200.
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>

<dl>
<dd>

**since:** `Optional<String>` — Relative time window, for example last_7d.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.ingestEvent(request) -> IngestResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().ingestEvent(
    IngestRequest
        .builder()
        .source("my-agent")
        .topic("conversations")
        .eventType("message.sent")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `IngestRequest` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.ingestEventBatch(request) -> IngestResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:write. Maximum 1000 events per batch; larger batches are rejected with 422. Request bodies over the size limit are rejected with 413. Each request consumes 10 rate-limit units.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().ingestEventBatch(
    Arrays.asList(
        IngestRequest
            .builder()
            .source("my-agent")
            .topic("conversations")
            .eventType("message.sent")
            .build()
    )
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**request:** `List<IngestRequest>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.events.streamEvents() -> Iterable&amp;lt;EventResult&amp;gt;</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. A Server-Sent Events stream of events matching the optional filters, held open indefinitely.

Opening a stream consumes 5 rate-limit units.

Each message has `event: event` and a `data` field carrying one EventResult as JSON. A comment line arrives every 15 seconds so intermediaries do not close an idle connection.

Every message carries an opaque, stream-specific `id` backed by a monotonic per-tenant delivery sequence. It records ingestion order, independently of the source event's `event_time`. Record the last id you processed and do not parse or construct it.

When `Last-Event-ID` is present, the server first establishes the live subscription, replays matching stored events strictly after that position in ascending order, and then continues with live delivery. Events committed at the history-to-live boundary may be delivered more than once, so consumers should deduplicate by `event_id`. This provides at-least-once delivery across a reconnect without leaving a gap.

Replay is limited to 1000 matching events. An older position returns 409 before the stream opens. Slow consumers are disconnected when the bounded live buffer fills and should reconnect with their last processed id. Concurrent streams are limited per tenant and may return 429 with `Retry-After`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.events().streamEvents(
    StreamEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**source:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**eventType:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityType:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityId:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**lastEventId:** `Optional<String>` — Opaque id from the last SSE message the client processed.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## timeline
<details><summary><code>client.timeline.getTimeline(entityType, entityId) -> EventPage</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. Cursor paginated, newest first.

Pass `cursor` from `next_cursor` to read the following page, and stop when `has_more` is false. The cursor is opaque: it is a keyset over `(event_time, event_id)`, it is exclusive so a row cannot repeat across pages, and its encoding may change without notice. Do not parse or construct one.

`include_linked=true` selects a different read that also returns causally linked events. That read is not paginated: it returns one page with `has_more` false, and it cannot be combined with `limit` or `cursor`. `since` is only available on that read, because the paginated read has no time filter.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.timeline().getTimeline(
    "entity_type",
    "entity_id",
    GetTimelineRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**entityType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**entityId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Page size. Values above the maximum are reduced to it, not rejected.
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque cursor from a previous response's next_cursor
    
</dd>
</dl>

<dl>
<dd>

**since:** `Optional<String>` — Relative time window, for example last_7d.
    
</dd>
</dl>

<dl>
<dd>

**includeLinked:** `Optional<Boolean>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## search
<details><summary><code>client.search.events(request) -> EventListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. The page size is capped at 200 and a cursor can advance through at most 1,000 relevance-ranked results. Each request consumes 5 rate-limit units.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.search().events(
    SearchRequest
        .builder()
        .query("query")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**query:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**source:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityType:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**entityId:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned by the preceding search page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## discover
<details><summary><code>client.discover.listSources() -> SourceListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. Returns the complete source metadata set without pagination.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.discover().listSources();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.discover.listEntityTypes() -> EntityTypeListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. Returns the complete entity-type metadata set without pagination.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.discover().listEntityTypes();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.discover.listEntities(entityType) -> EntityListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. Entities are ordered by event count and entity ID. The limit is capped at 200; pass `next_cursor` as `cursor`.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.discover().listEntities(
    "entity_type",
    ListEntitiesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**entityType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` — Page size. Values above 200 are clamped to 200.
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.discover.getEventSchema(source, eventType) -> SourceSchema</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.discover().getEventSchema(
    "source",
    "event_type",
    GetEventSchemaRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**source:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**eventType:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## links
<details><summary><code>client.links.addEntityRef(request) -> StatusResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.links().addEntityRef(
    AddEntityRefRequest
        .builder()
        .eventId("event_id")
        .entityType("entity_type")
        .entityId("entity_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**eventId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**entityType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**entityId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**createdBy:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.links.createEventLink(request) -> CreateLinkResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.links().createEventLink(
    CreateLinkRequest
        .builder()
        .sourceEventId("source_event_id")
        .targetEventId("target_event_id")
        .linkType("link_type")
        .confidence(1.1)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**sourceEventId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**targetEventId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**linkType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**confidence:** `Double` 
    
</dd>
</dl>

<dl>
<dd>

**reasoning:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**createdBy:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.links.linkEntities(request) -> LinkEntityResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.links().linkEntities(
    LinkEntityRequest
        .builder()
        .fromEntityType("from_entity_type")
        .fromEntityId("from_entity_id")
        .toEntityType("to_entity_type")
        .toEntityId("to_entity_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**fromEntityType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**fromEntityId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**toEntityType:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**toEntityId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**createdBy:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.links.traverseGraph(request) -> EventListResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope events:read or events:write. The traversal is bounded by `max_depth`, is not cursor-paginated, and consumes 5 rate-limit units.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.links().traverseGraph(
    GraphRequest
        .builder()
        .startEventId("start_event_id")
        .direction(GraphRequestDirection.OUTGOING)
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**startEventId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**direction:** `GraphRequestDirection` 
    
</dd>
</dl>

<dl>
<dd>

**linkTypes:** `Optional<List<String>>` 
    
</dd>
</dl>

<dl>
<dd>

**maxDepth:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**minConfidence:** `Optional<Double>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## sdk
<details><summary><code>client.sdk.identifyUser(request) -> AcceptedResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope users:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.sdk().identifyUser(
    IdentifyUserRequest
        .builder()
        .userId("user_id")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**userId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**traits:** `Optional<Map<String, Object>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.sdk.trackSignals(request) -> AcceptedResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope signals:write. Maximum 1000 signals per request; larger batches are rejected with 422. Each request consumes 10 rate-limit units.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.sdk().trackSignals(
    TrackSignalsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**signals:** `Optional<List<SignalRequest>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.sdk.trackTraces(request) -> AcceptedResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope traces:write. Maximum 1000 traces or total spans per request; larger batches are rejected with 422. Each request consumes 10 rate-limit units.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.sdk().trackTraces(
    TrackTracesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**traces:** `Optional<List<TraceRequest>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## agents
<details><summary><code>client.agents.listAgents() -> List&amp;lt;AgentSummary&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().listAgents();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.searchAgentHashIndex() -> List&amp;lt;HashIndexEntry&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().searchAgentHashIndex(
    SearchAgentHashIndexRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**q:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**domains:** `Optional<String>` — Comma-separated hash domains.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.subscribeToAgentChanges() -> Iterable&amp;lt;Map&amp;lt;String, Object&amp;gt;&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().subscribeToAgentChanges();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.updateAgent(name, request) -> AgentSummary</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().updateAgent(
    "name",
    UpdateAgentRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**environment:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**owner:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**purpose:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.getAgentSnapshot(name) -> Optional&amp;lt;AgentSnapshot&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().getAgentSnapshot(
    "name",
    GetAgentSnapshotRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.pinLatestAgentVersion(name) -> AgentSummary</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().pinLatestAgentVersion(
    "name",
    PinLatestAgentVersionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.createAgentChatSession(name) -> CreateAgentChatSessionResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().createAgentChatSession(
    "name",
    CreateAgentChatSessionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.getAgentChatSession(name, sessionId) -> AgentChatSession</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().getAgentChatSession(
    "name",
    "session_id",
    GetAgentChatSessionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**sessionId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.sendAgentChatMessage(name, sessionId, request) -> SendAgentChatMessageResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().sendAgentChatMessage(
    "name",
    "session_id",
    SendAgentChatMessageRequest
        .builder()
        .text("text")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**sessionId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**text:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.registerAgentArtifact(request) -> AgentVersionSummary</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope agents:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().registerAgentArtifact(
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
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**artifact:** `RegisterAgentArtifactRequestArtifact` 
    
</dd>
</dl>

<dl>
<dd>

**metadata:** `Optional<RegisterAgentArtifactRequestMetadata>` — Mutable, human-authored metadata attached to a logical Agent identity. Artifact configuration remains immutable inside `AgentRegistryVersionRecord`.
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<RegisterAgentArtifactRequestStatus>` — Defaults to `current`. Registering a new current version atomically demotes the previous current version to stable.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.agents.recordAgentRuns(request) -> RecordAgentRunsResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Requires scope agents:write.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.agents().recordAgentRuns(
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
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**runs:** `List<RecordAgentRunsRequestRunsItem>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## datasets
<details><summary><code>client.datasets.listDatasets() -> TaskSuitePage</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasets(
    ListDatasetsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**includeArchived:** `Optional<Boolean>` 
    
</dd>
</dl>

<dl>
<dd>

**query:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.createDataset(request) -> TaskSuite</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().createDataset(
    CreateTaskSuitePayload
        .builder()
        .name("name")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**purpose:** `Optional<CreateTaskSuitePayloadPurpose>` — Intended use of a dataset — drives the colored badge on the picker and lets apps route additions to the right backend (eval suite, training set, replay corpus, manual review queue).
    
</dd>
</dl>

<dl>
<dd>

**tags:** `Optional<List<String>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.createDatasetWithTrace(request) -> CreateTaskSuiteWithTraceResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().createDatasetWithTrace(
    CreateTaskSuiteWithTraceRequest
        .builder()
        .dataset(
            CreateTaskSuiteWithTraceRequestDataset
                .builder()
                .name("name")
                .build()
        )
        .trace(
            CreateTaskSuiteWithTraceRequestTrace
                .builder()
                .traceId("traceId")
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**dataset:** `CreateTaskSuiteWithTraceRequestDataset` 
    
</dd>
</dl>

<dl>
<dd>

**trace:** `CreateTaskSuiteWithTraceRequestTrace` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.getDataset(datasetId) -> TaskSuiteDetail</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().getDataset(
    "dataset_id",
    GetDatasetRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.archiveDataset(datasetId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().archiveDataset(
    "dataset_id",
    ArchiveDatasetRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**cascade:** `Optional<Boolean>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.updateDataset(datasetId, request) -> TaskSuite</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().updateDataset(
    "dataset_id",
    TaskSuitePatch
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**purpose:** `Optional<TaskSuitePatchPurpose>` — Intended use of a dataset — drives the colored badge on the picker and lets apps route additions to the right backend (eval suite, training set, replay corpus, manual review queue).
    
</dd>
</dl>

<dl>
<dd>

**tags:** `Optional<List<String>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.getDatasetSnapshot(datasetId) -> TaskSuiteSnapshot</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().getDatasetSnapshot(
    "dataset_id",
    GetDatasetSnapshotRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetTraces(datasetId) -> TaskPage</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetTraces(
    "dataset_id",
    ListDatasetTracesRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.addTraceToDataset(datasetId, request) -> AddTaskFromTraceResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().addTraceToDataset(
    "dataset_id",
    AddTaskFromTraceRequest
        .builder()
        .traceId("traceId")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**eventIds:** `Optional<List<String>>` — Accepted for compatibility but never trusted as the authoritative capture. The service re-reads the canonical store by subject.
    
</dd>
</dl>

<dl>
<dd>

**addTaskFromTraceRequestIdempotencyKey:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**notes:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**split:** `Optional<AddTaskFromTraceRequestSplit>` — Train / validation / test split assignment.
    
</dd>
</dl>

<dl>
<dd>

**task:** `Optional<AddTaskFromTraceRequestTask>` — Optional task fields. Anything left unset is derived from the captured trace (title from the label, instruction from the first message, expected outcome from the events after the cutoff).
    
</dd>
</dl>

<dl>
<dd>

**traceId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**traceSynthesized:** `Optional<Boolean>` 
    
</dd>
</dl>

<dl>
<dd>

**verifiers:** `Optional<List<AddTaskFromTraceRequestVerifiersItem>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.updateDatasetTraces(datasetId, request)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().updateDatasetTraces(
    "dataset_id",
    UpdateTracesRequest
        .builder()
        .patch(
            UpdateTracesRequestPatch
                .builder()
                .build()
        )
        .traceIds(
            Arrays.asList("traceIds")
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**patch:** `UpdateTracesRequestPatch` — Patch to apply to one or more memberships. Nullable annotations preserve the same three states as [`PatchField`]: explicit JSON `null` clears while omission is a no-op.
    
</dd>
</dl>

<dl>
<dd>

**traceIds:** `List<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.removeTraceFromDataset(datasetId, membershipId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().removeTraceFromDataset(
    "dataset_id",
    "membership_id",
    RemoveTraceFromDatasetRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**reason:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.refreshDatasetTrace(datasetId, membershipId, request) -> TaskMembership</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().refreshDatasetTrace(
    "dataset_id",
    "membership_id",
    RefreshDatasetTraceRequest
        .builder()
        .body(
            RefreshMembershipRequest
                .builder()
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**request:** `RefreshMembershipRequest` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetTraceEvents(datasetId, membershipId) -> TaskEventPage</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetTraceEvents(
    "dataset_id",
    "membership_id",
    ListDatasetTraceEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listTraceDatasetMemberships(traceId) -> List&amp;lt;TaskMembership&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listTraceDatasetMemberships(
    "trace_id",
    ListTraceDatasetMembershipsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**traceId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetTasks(datasetId) -> TaskPage</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetTasks(
    "dataset_id",
    ListDatasetTasksRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.createDatasetTask(datasetId, request) -> Task</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().createDatasetTask(
    "dataset_id",
    CreateDatasetTaskRequest
        .builder()
        .body(
            CreateTaskRequest.of(new 
            HashMap<String, Object>() {{put("key", "value");
            }})
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**request:** `Object` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.getDatasetTask(datasetId, membershipId) -> Task</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().getDatasetTask(
    "dataset_id",
    "membership_id",
    GetDatasetTaskRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.deleteDatasetTask(datasetId, membershipId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().deleteDatasetTask(
    "dataset_id",
    "membership_id",
    DeleteDatasetTaskRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**reason:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.updateDatasetTask(datasetId, membershipId, request) -> Task</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().updateDatasetTask(
    "dataset_id",
    "membership_id",
    UpdateDatasetTaskRequest
        .builder()
        .body(
            UpdateTaskRequest.of(new 
            HashMap<String, Object>() {{put("key", "value");
            }})
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**request:** `Object` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.setDatasetTaskVerifiers(datasetId, membershipId, request) -> Task</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().setDatasetTaskVerifiers(
    "dataset_id",
    "membership_id",
    SetTaskVerifiersRequest
        .builder()
        .verifiers(
            Arrays.asList(
                SetTaskVerifiersRequestVerifiersItem
                    .builder()
                    .scorerId("scorerId")
                    .build()
            )
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**verifiers:** `List<SetTaskVerifiersRequestVerifiersItem>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetTaskEvents(datasetId, membershipId) -> TaskEventPage</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetTaskEvents(
    "dataset_id",
    "membership_id",
    ListDatasetTaskEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**cursor:** `Optional<String>` — Opaque position returned as `next_cursor` by the preceding page.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.refreshDatasetTask(datasetId, membershipId, request) -> TaskMembership</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().refreshDatasetTask(
    "dataset_id",
    "membership_id",
    RefreshDatasetTaskRequest
        .builder()
        .body(
            RefreshMembershipRequest
                .builder()
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**membershipId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**request:** `RefreshMembershipRequest` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetClusters(datasetId) -> List&amp;lt;DatasetCluster&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetClusters(
    "dataset_id",
    ListDatasetClustersRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.createDatasetCluster(datasetId, request) -> DatasetCluster</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().createDatasetCluster(
    "dataset_id",
    CreateClusterRequest
        .builder()
        .color("color")
        .label("label")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**color:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**createClusterRequestIdempotencyKey:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**label:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**similarityCenter:** `Optional<List<Double>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.deleteDatasetCluster(datasetId, clusterId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().deleteDatasetCluster(
    "dataset_id",
    "cluster_id",
    DeleteDatasetClusterRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**clusterId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.updateDatasetCluster(datasetId, clusterId, request) -> DatasetCluster</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().updateDatasetCluster(
    "dataset_id",
    "cluster_id",
    UpdateClusterRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**clusterId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**color:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**label:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**similarityCenter:** `Optional<List<Double>>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetSavedViews(datasetId) -> List&amp;lt;DatasetSavedView&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetSavedViews(
    "dataset_id",
    ListDatasetSavedViewsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.createDatasetSavedView(datasetId, request) -> DatasetSavedView</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().createDatasetSavedView(
    "dataset_id",
    CreateSavedViewRequest
        .builder()
        .name("name")
        .scope(CreateSavedViewRequestScope.PERSONAL)
        .state(
            CreateSavedViewRequestState
                .builder()
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**createSavedViewRequestIdempotencyKey:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**schemaVersion:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**scope:** `CreateSavedViewRequestScope` 
    
</dd>
</dl>

<dl>
<dd>

**shortcut:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**state:** `CreateSavedViewRequestState` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.deleteDatasetSavedView(datasetId, viewId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().deleteDatasetSavedView(
    "dataset_id",
    "view_id",
    DeleteDatasetSavedViewRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**viewId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.updateDatasetSavedView(datasetId, viewId, request) -> DatasetSavedView</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().updateDatasetSavedView(
    "dataset_id",
    "view_id",
    DatasetSavedViewPatch
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**viewId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**createdBy:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**scope:** `Optional<DatasetSavedViewPatchScope>` 
    
</dd>
</dl>

<dl>
<dd>

**shortcut:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**state:** `Optional<DatasetSavedViewPatchState>` 
    
</dd>
</dl>

<dl>
<dd>

**updatedAt:** `Optional<OffsetDateTime>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetVersions(datasetId) -> List&amp;lt;TaskSuiteVersion&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetVersions(
    "dataset_id",
    ListDatasetVersionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.publishDatasetVersion(datasetId, request) -> TaskSuiteVersion</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().publishDatasetVersion(
    "dataset_id",
    PublishVersionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**publishVersionRequestIdempotencyKey:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**label:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.getDatasetVersion(datasetId, versionId) -> TaskSuiteSnapshot</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().getDatasetVersion(
    "dataset_id",
    "version_id",
    GetDatasetVersionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**versionId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.datasets.listDatasetEvaluationRuns(datasetId) -> List&amp;lt;TaskSuiteEvalRun&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.datasets().listDatasetEvaluationRuns(
    "dataset_id",
    ListDatasetEvaluationRunsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**datasetId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## environments
<details><summary><code>client.environments.listEnvironments() -> ListEnvironmentsResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().listEnvironments();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.createEnvironment(request) -> EnvironmentResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().createEnvironment(
    CreateEnvironmentRequest
        .builder()
        .slug("slug")
        .label("label")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**slug:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**label:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**description:** `Optional<String>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.getEnvironment(environmentId) -> EnvironmentResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().getEnvironment(
    "environment_id",
    GetEnvironmentRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**environmentId:** `String` — Environment ID or slug.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.listEnvironmentVersions(environmentId) -> List&amp;lt;EnvironmentVersionRecord&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().listEnvironmentVersions(
    "environment_id",
    ListEnvironmentVersionsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**environmentId:** `String` — Environment ID or slug.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.createEnvironmentVersion(environmentId, request) -> EnvironmentVersionResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().createEnvironmentVersion(
    "environment_id",
    CreateEnvironmentVersionRequest
        .builder()
        .version("version")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**environmentId:** `String` — Environment ID or slug.
    
</dd>
</dl>

<dl>
<dd>

**version:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**spec:** `Optional<EnvironmentSpec>` 
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<EnvironmentVersionStatus>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.getEnvironmentVersion(environmentId, versionSelector) -> EnvironmentVersionResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().getEnvironmentVersion(
    "environment_id",
    "version_selector",
    GetEnvironmentVersionRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**environmentId:** `String` — Environment ID or slug.
    
</dd>
</dl>

<dl>
<dd>

**versionSelector:** `String` — Environment-version ID or version label.
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.environments.compileEnvironmentVersion(environmentId, versionSelector, request) -> CompileEnvironmentResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.environments().compileEnvironmentVersion(
    "environment_id",
    "version_selector",
    CompileEnvironmentRequest
        .builder()
        .datasetSnapshotId("datasetSnapshotId")
        .scenarioId("scenarioId")
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**environmentId:** `String` — Environment ID or slug.
    
</dd>
</dl>

<dl>
<dd>

**versionSelector:** `String` — Environment-version ID or version label.
    
</dd>
</dl>

<dl>
<dd>

**datasetSnapshotId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**scenarioId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## backtests
<details><summary><code>client.backtests.getBacktestsAvailability() -> BacktestsAvailability</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().getBacktestsAvailability();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.listBacktestJobs() -> ListBacktestJobsResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().listBacktestJobs(
    ListBacktestJobsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**mode:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**status:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**offset:** `Optional<Integer>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.createBacktestJob(request) -> CreateBacktestJobResponse</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

Returns 202 after the durable job and its trials have been admitted.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().createBacktestJob(
    CreateBacktestJobRequest
        .builder()
        .name("name")
        .recipe(
            CreateBacktestJobRequestRecipe
                .builder()
                .data(
                    CreateBacktestJobRequestRecipeData
                        .builder()
                        .kind(CreateBacktestJobRequestRecipeDataKind.COMPOSED)
                        .scenarios(
                            Arrays.asList(
                                CreateBacktestJobRequestRecipeDataScenariosItem
                                    .builder()
                                    .count(1)
                                    .id("id")
                                    .kind(CreateBacktestJobRequestRecipeDataScenariosItemKind.ADVERSARIAL)
                                    .label("label")
                                    .build()
                            )
                        )
                        .sources(
                            Arrays.asList(
                                CreateBacktestJobRequestRecipeDataSourcesItem
                                    .builder()
                                    .count(1)
                                    .id("id")
                                    .kind(CreateBacktestJobRequestRecipeDataSourcesItemKind.PROD)
                                    .label("label")
                                    .build()
                            )
                        )
                        .build()
                )
                .mode(CreateBacktestJobRequestRecipeMode.REPLAY)
                .name("name")
                .agents(
                    Arrays.asList(
                        CreateBacktestJobRequestRecipeAgentsItem
                            .builder()
                            .hue("hue")
                            .id("id")
                            .label("label")
                            .notes("notes")
                            .build()
                    )
                )
                .graders(
                    Arrays.asList(
                        CreateBacktestJobRequestRecipeGradersItem
                            .builder()
                            .id("id")
                            .kind(CreateBacktestJobRequestRecipeGradersItemKind.RUBRIC)
                            .label("label")
                            .source(CreateBacktestJobRequestRecipeGradersItemSource.PROPOSED)
                            .weight(CreateBacktestJobRequestRecipeGradersItemWeight.LOW)
                            .build()
                    )
                )
                .build()
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**idempotencyKey:** `Optional<String>` — Optional caller-generated key for safely retrying a mutation.
    
</dd>
</dl>

<dl>
<dd>

**cases:** `Optional<List<CreateBacktestJobRequestCasesItem>>` 
    
</dd>
</dl>

<dl>
<dd>

**evaluatorProfileId:** `Optional<String>` 
    
</dd>
</dl>

<dl>
<dd>

**nConcurrent:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**recipe:** `CreateBacktestJobRequestRecipe` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.getBacktestJob(jobId) -> BacktestJobDetailResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().getBacktestJob(
    "job_id",
    GetBacktestJobRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**jobId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.listBacktestJobTrials(jobId) -> ListBacktestJobTrialsResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().listBacktestJobTrials(
    "job_id",
    ListBacktestJobTrialsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**jobId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**limit:** `Optional<Integer>` 
    
</dd>
</dl>

<dl>
<dd>

**offset:** `Optional<Integer>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.getBacktestTrial(jobId, trialId) -> BacktestTrialDetailResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().getBacktestTrial(
    "job_id",
    "trial_id",
    GetBacktestTrialRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**jobId:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**trialId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.cancelBacktestJob(jobId) -> CancelBacktestJobResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().cancelBacktestJob(
    "job_id",
    CancelBacktestJobRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**jobId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.backtests.streamBacktestJobEvents(jobId) -> Iterable&amp;lt;TrialEvent&amp;gt;</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.backtests().streamBacktestJobEvents(
    "job_id",
    StreamBacktestJobEventsRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**jobId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

## credentials
<details><summary><code>client.credentials.listSdkKeys() -> SdkKeyListResponse</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.credentials().listSdkKeys();
```
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.credentials.createSdkKey(request) -> CreatedSdkKey</code></summary>
<dl>
<dd>

#### 📝 Description

<dl>
<dd>

<dl>
<dd>

The bearer secret is returned once and is not stored in plaintext.
</dd>
</dl>
</dd>
</dl>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.credentials().createSdkKey(
    CreateSdkKeyRequest
        .builder()
        .name("name")
        .scopes(
            Arrays.asList(CreateSdkKeyRequestScopesItem.TRACES_WRITE)
        )
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**name:** `String` 
    
</dd>
</dl>

<dl>
<dd>

**scopes:** `List<CreateSdkKeyRequestScopesItem>` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

<details><summary><code>client.credentials.revokeSdkKey(keyId)</code></summary>
<dl>
<dd>

#### 🔌 Usage

<dl>
<dd>

<dl>
<dd>

```java
client.credentials().revokeSdkKey(
    "key_id",
    RevokeSdkKeyRequest
        .builder()
        .build()
);
```
</dd>
</dl>
</dd>
</dl>

#### ⚙️ Parameters

<dl>
<dd>

<dl>
<dd>

**keyId:** `String` 
    
</dd>
</dl>
</dd>
</dl>


</dd>
</dl>
</details>

