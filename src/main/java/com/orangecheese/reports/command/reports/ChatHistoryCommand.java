package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.chathistory.ChatHistoryFetchRequest;
import com.orangecheese.reports.core.http.response.ChatHistoryEntry;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.utility.PlayerUtility;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class ChatHistoryCommand implements ICommandArgument {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    public ChatHistoryCommand() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String targetId = arguments[1];

        PlayerProfile targetProfile;
        try {
            UUID targetUuid = UUID.fromString(targetId);
            targetProfile = PlayerUtility.getProfile(targetUuid);
        } catch(IllegalArgumentException e) {
            PlayerProfile profile = PlayerUtility.getProfile(targetId);
            if(profile.getUniqueId() == null) {
                player.sendMessage(ChatColor.RED + "You have given an invalid player under the identity of '" + targetId + "'!");
                return;
            }
            targetProfile = profile;
        }

        String pageString = arguments[2];
        int page;
        try {
            page = Integer.parseInt(pageString);
        } catch(NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "You have given an invalid page number!");
            return;
        }

        final PlayerProfile finalTargetProfile = targetProfile;

        ChatHistoryFetchRequest fetchRequest = new ChatHistoryFetchRequest(targetProfile.getUniqueId(), page, containerCache.getAccessToken(), response -> {
            ChatHistoryEntry[] historyEntries = response.getHistory();
            int currentPage = response.getPage();
            int maxPages = response.getPages();

            String targetName = finalTargetProfile.getName();
            String pageNumbers = "" + ChatColor.RED + currentPage + ChatColor.GRAY + "/" + ChatColor.RED + maxPages;
            player.sendMessage(ChatColor.GRAY + "Chat history of '" + ChatColor.RED + targetName + ChatColor.GRAY + "' (" + pageNumbers + ChatColor.GRAY + "):");

            if(historyEntries.length > 0) {
                for(ChatHistoryEntry chatHistoryEntry : historyEntries) {
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy HH:mm:ss");
                    String formattedDateTime = dateFormatter.format(chatHistoryEntry.getCreatedAt());
                    String message =
                            ChatColor.GRAY + "[" +
                            ChatColor.DARK_GRAY + formattedDateTime +
                            ChatColor.GRAY + "] " +
                            ChatColor.RED + "» " +
                            ChatColor.GRAY + chatHistoryEntry.getMessage();
                    player.sendMessage(message);
                }
            } else {
                player.sendMessage(ChatColor.GRAY + "No entries.");
            }
        });

        apiManager.makeRequest(fetchRequest);
    }

    @Override
    public String getBaseCommandArgument() {
        return "chathistory";
    }

    @Override
    public String getUsage() {
        return "<Player/Player UUID> <page>";
    }

    @Override
    public int getRequiredArguments() {
        return 3;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}