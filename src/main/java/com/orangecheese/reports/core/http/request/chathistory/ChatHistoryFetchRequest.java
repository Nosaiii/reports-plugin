package com.orangecheese.reports.core.http.request.chathistory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.core.http.request.HTTPMethod;
import com.orangecheese.reports.core.http.request.HTTPRequestWithResponse;
import com.orangecheese.reports.core.http.request.IHTTPBody;
import com.orangecheese.reports.core.http.response.ChatHistoryEntry;
import com.orangecheese.reports.core.http.response.ChatHistoryResponse;
import com.orangecheese.reports.utility.DateUtility;
import com.orangecheese.reports.utility.EmptyRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;

public class ChatHistoryFetchRequest extends HTTPRequestWithResponse<ChatHistoryResponse, EmptyRecord> implements IHTTPBody {
    private final UUID playerUuid;

    private final int page;

    private final String accessToken;

    public ChatHistoryFetchRequest(
            UUID playerUuid,
            int page,
            String accessToken,
            Consumer<ChatHistoryResponse> onSuccess) {
        super(
                "chat-history/fetch",
                HTTPMethod.GET,
                onSuccess,
                body -> ReportsPlugin.getInstance().getLogger().log(Level.SEVERE, "Something went wrong fetching the chat history! Is the plugin linked to a container?"));
        this.playerUuid = playerUuid;
        this.page = page;
        this.accessToken = accessToken;
    }

    @Override
    public ChatHistoryResponse parseResponse(JsonObject json) {
        JsonObject chatHistoryRootObject = json.get("history").getAsJsonObject();

        JsonArray chatHistoryJsonArray = chatHistoryRootObject.get("data").getAsJsonArray();
        Iterator<JsonElement> chatHistoryIterator = chatHistoryJsonArray.iterator();

        List<ChatHistoryEntry> chatHistory = new ArrayList<>();

        while(chatHistoryIterator.hasNext()) {
            JsonElement chatHistoryElement = chatHistoryIterator.next();
            JsonObject chatHistoryObject = chatHistoryElement.getAsJsonObject();

            int id = chatHistoryObject.get("id").getAsInt();
            int containerId = chatHistoryObject.get("container_id").getAsInt();
            UUID playerUuid = UUID.fromString(chatHistoryObject.get("player_uuid").getAsString());
            String message = chatHistoryObject.get("message").getAsString();

            LocalDateTime createdAt, updatedAt;
            try {
                createdAt = DateUtility.isoDateTimeToLocalDateTime(chatHistoryObject.get("created_at").getAsString());
                updatedAt = DateUtility.isoDateTimeToLocalDateTime(chatHistoryObject.get("updated_at").getAsString());
            } catch (DateTimeParseException e) {
                throw new RuntimeException(e);
            }

            ChatHistoryEntry chatHistoryResponse = new ChatHistoryEntry(id, containerId, playerUuid, message, createdAt, updatedAt);
            chatHistory.add(chatHistoryResponse);
        }

        ChatHistoryEntry[] chatHistoryArray = chatHistory.toArray(ChatHistoryEntry[]::new);
        int page = json.get("page").getAsInt();
        int pages = json.get("pages").getAsInt();

        return new ChatHistoryResponse(chatHistoryArray, page, pages);
    }

    @Override
    public EmptyRecord parseFailureResponse(JsonObject json) {
        return new EmptyRecord();
    }

    @Override
    public JsonObject generateJson() {
        JsonObject json = new JsonObject();
        json.addProperty("accessToken", accessToken);
        json.addProperty("playerUuid", playerUuid.toString());
        json.addProperty("page", page);
        return json;
    }
}
