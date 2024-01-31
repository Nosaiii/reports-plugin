package com.orangecheese.reports.service.chatprompt;

import java.util.function.Function;

public class ChatPromptPlaceholder {
    private final String key;

    private final Function<String, String> transformer;

    public ChatPromptPlaceholder(String key, Function<String, String> transformer) {
        this.key = key;
        this.transformer = transformer;
    }

    public ChatPromptPlaceholder(String key) {
        this(key, null);
    }

    public String getKey() {
        return key;
    }

    public String transform(String value) {
        if(transformer == null)
            return value;
        return transformer.apply(value);
    }
}