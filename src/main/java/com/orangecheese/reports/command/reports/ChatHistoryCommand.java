package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.command.BaseCommand;
import com.orangecheese.reports.command.ICommandArgument;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.chathistory.ChatHistoryFetchRequest;
import com.orangecheese.reports.core.http.response.ChatHistoryEntry;
import com.orangecheese.reports.core.io.ContainerCache;
import com.orangecheese.reports.service.PlayerProfileService;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;

import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.function.Consumer;

public class ChatHistoryCommand implements ICommandArgument {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    private final PlayerProfileService playerProfileService;

    public ChatHistoryCommand() {
        apiManager = ServiceContainer.get(APIManager.class);
        containerCache = ServiceContainer.get(ContainerCache.class);
        playerProfileService = ServiceContainer.get(PlayerProfileService.class);
    }

    @Override
    public void execute(Player player, String[] arguments, BaseCommand baseCommand) {
        String targetId = arguments[1];

        fetchPlayerProfile(player, targetId, targetProfile -> {
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
                player.sendMessage("");
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

                TextComponent previousPageComponent = new TextComponent("[Previous page]");
                TextComponent buttonSpacingComponent = new TextComponent(" ");
                TextComponent nextPageComponent = new TextComponent("[Next page]");

                String baseCommandString = "/reports chathistory " + finalTargetProfile.getUniqueId();
                int previousPage = currentPage - 1;
                int nextPage = currentPage + 1;

                previousPageComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, baseCommandString + " " + previousPage));
                previousPageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("← Go to the previous page")));
                nextPageComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, baseCommandString + " " + nextPage));
                nextPageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Go to the next page →")));

                previousPageComponent.setColor(net.md_5.bungee.api.ChatColor.RED);
                nextPageComponent.setColor(net.md_5.bungee.api.ChatColor.RED);

                player.spigot().sendMessage(previousPageComponent, buttonSpacingComponent, nextPageComponent);

                player.sendMessage("");
            });

            apiManager.makeRequest(fetchRequest);
        });
    }

    private void fetchPlayerProfile(Player player, String targetId, Consumer<PlayerProfile> onSuccess) {
        try {
            UUID targetUuid = UUID.fromString(targetId);
            playerProfileService.getWithCatch(
                    targetUuid,
                    onSuccess,
                    messageResponse -> player.sendMessage(ChatColor.RED + "You have given an invalid player under the identity of '" + targetId + "'!"));
        } catch(IllegalArgumentException e) {
            playerProfileService.getWithCatch(
                    targetId,
                    onSuccess,
                    messageResponse -> player.sendMessage(ChatColor.RED + "You have given an invalid player under the identity of '" + targetId + "'!"));
        }
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
