package com.orangecheese.reports.service.chatprompt;

public class ChatPromptArgument {
    private final String prompt;

    private String value;

    private ChatPromptPlaceholder placeholder;

    private ChatPromptCondition condition;

    public ChatPromptArgument(String prompt) {
        this.prompt = prompt;
    }

    public void setValue(String value) {
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