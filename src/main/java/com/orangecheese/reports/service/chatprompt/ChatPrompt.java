package com.orangecheese.reports.service.chatprompt;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.event.ChatPromptEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ChatPrompt {
    private final Player player;

    private final Consumer<String[]> onFinish;

    private final ArrayList<ChatPromptArgument> arguments;

    private int currentArgumentIndex;

    private ChatPromptEvent event;

    private final Map<String, String> placeholders;

    private static final HashMap<Player, ChatPrompt> runningPrompts;

    static {
        runningPrompts = new HashMap<>();
    }

    public ChatPrompt(Player player, Consumer<String[]> onFinish) {
        this.player = player;
        this.onFinish = onFinish;
        arguments = new ArrayList<>();
        placeholders = new HashMap<>();
    }

    public void addArgument(ChatPromptArgument argument) {
        arguments.add(argument);
    }

    public void start() {
        if(runningPrompts.containsKey(player)) {
            runningPrompts.get(player).quit();
        }
        runningPrompts.put(player, this);

        event = new ChatPromptEvent(this);

        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(event, ReportsPlugin.getInstance());

        player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(Type 'cancel' to stop the prompt right away)");
        prompt();
    }

    public void cancel() {
        quit();
        player.sendMessage(ChatColor.GRAY + "(The prompt has been cancelled)");
        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 3.0f, 4.0f);
    }

    public void quit() {
        HandlerList.unregisterAll(event);
        runningPrompts.remove(player);
    }

    public void finish() {
        quit();
        String[] argumentValues = arguments.stream().map(ChatPromptArgument::getValue).toArray(String[]::new);
        onFinish.accept(argumentValues);
    }

    public void process(String value) {
        ChatPromptArgument currentArgument = arguments.get(currentArgumentIndex);
        currentArgument.setValue(value);

        if(!currentArgument.testCondition()) {
            player.sendMessage(ChatColor.RED + currentArgument.getConditionMessage());
            return;
        }

        ChatPromptPlaceholder placeholder = currentArgument.getPlaceholder();
        if(placeholder != null)
            placeholders.put(placeholder.getKey(), placeholder.transform(value));

        player.sendMessage(ChatColor.RED + "» " + ChatColor.DARK_GRAY + value);
        player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 3.0f, 4.0f);

        next();
    }

    public void prompt() {
        ChatPromptArgument argument = arguments.get(currentArgumentIndex);

        String prompt = argument.getPrompt();
        for(Map.Entry<String, String> placeholderEntry : placeholders.entrySet())
            prompt = prompt.replace("%" + placeholderEntry.getKey() + "%", placeholderEntry.getValue());

        player.sendMessage(ChatColor.GRAY + prompt);
    }

    private void next() {
        currentArgumentIndex++;
        if(currentArgumentIndex >= arguments.size()) {
            finish();
            return;
        }

        prompt();
    }

    public Player getPlayer() {
        return player;
    }
}