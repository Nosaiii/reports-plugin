package com.orangecheese.reports.service.chatprompt;

import org.bukkit.ChatColor;

public class ChatPromptArgument {
    private final String prompt;

    private final boolean stripColors;

    private String value;

    private ChatPromptPlaceholder placeholder;

    private ChatPromptCondition condition;

    public ChatPromptArgument(String prompt, boolean stripColors) {
        this.prompt = prompt;
        this.stripColors = stripColors;
    }

    public void setValue(String value) {
        if(stripColors)
            value = ChatColor.stripColor(value);
        this.value = value;
    }

    public void setPlaceholder(ChatPromptPlaceholder placeholder) {
        this.placeholder = placeholder;
    }

    public void setCondition(ChatPromptCondition condition) {
        this.condition = condition;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getValue() {
        return value;
    }

    public ChatPromptPlaceholder getPlaceholder() {
        return placeholder;
    }

    public boolean testCondition() {
        if(condition == null)
            return true;

        return condition.test(value);
    }

    public String getConditionMessage() {
        String message = condition.getMessage();

        if(placeholder != null) {
            String replacementKey = "%" + placeholder.getKey() + "%";
            String transformedMessage = placeholder.transform(value);
            message = message.replace(replacementKey, transformedMessage);
        }

        return message;
    }
}