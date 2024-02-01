package com.orangecheese.reports.core.http.response;

public class ChatHistoryResponse {
    private final ChatHistoryEntry[] history;

    private final int page;

    private final int pages;

    public ChatHistoryResponse(ChatHistoryEntry[] history, int page, int pages) {
        this.history = history;
        this.page = page;
        this.pages = pages;
    }

    public ChatHistoryEntry[] getHistory() {
        return history;
    }

    public int getPage() {
        return page;
    }

    public int getPages() {
        return pages;
    }
}