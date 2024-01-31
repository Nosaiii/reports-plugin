package com.orangecheese.reports.service.chatprompt;

import java.util.function.Predicate;

public class ChatPromptCondition {
    private final Predicate<String> condition;

    private final String message;

    public ChatPromptCondition(Predicate<String> condition, String message) {
        this.condition = condition;
        this.message = message;
    }

    public boolean test(String value) {
        return condition.test(value);
    }

    public String getMessage() {
        return message;
    }
}