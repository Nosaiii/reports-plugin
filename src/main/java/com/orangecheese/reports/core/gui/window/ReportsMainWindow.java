package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.config.MenuConfig;
import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.abstraction.ReportsMenuNavigationItem;
import com.orangecheese.reports.core.gui.layout.padded.PaddedWindowLayout;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.ReadReportsMetaRequest;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReportsMainWindow extends Window {
    private final APIManager apiManager;

    public ReportsMainWindow(Player player) {
        super(player, "Reports", new PaddedWindowLayout(3, 1), false, new WindowHistory());
        apiManager = ServiceContainer.get(APIManager.class);
    }

    @Override
    public void onInitialize(int page) {
        MenuConfig menuConfig = ReportsPlugin.getInstance().getReportsConfig().getMenu();

        BugReportsWindow bugReportsWindow = new BugReportsWindow(getPlayer(), getHistory());
        PlayerReportsWindow playerReportsWindow = new PlayerReportsWindow(getPlayer(), getHistory());
        SuggestionReportsWindow suggestionReportsWindow = new SuggestionReportsWindow(getPlayer(), getHistory());

        ReportsMenuNavigationItem bugReports = new ReportsMenuNavigationItem(this, bugReportsWindow, menuConfig.getMainIcon("bugs"), "Bug reports");
        ReportsMenuNavigationItem playerReports = new ReportsMenuNavigationItem(this, playerReportsWindow, menuConfig.getMainIcon("players"), "Player reports");
        ReportsMenuNavigationItem suggestionReports = new ReportsMenuNavigationItem(this, suggestionReportsWindow, menuConfig.getMainIcon("suggestions"), "Suggestions");

        addItem(bugReports);
        addItem(playerReports);
        addItem(suggestionReports);

        ReadReportsMetaRequest reportsMetaRequest = new ReadReportsMetaRequest(
                response -> {
                    bugReports.setReports(getPlayer(), response.getBugReports());
                    playerReports.setReports(getPlayer(), response.getPlayerReports());
                    suggestionReports.setReports(getPlayer(), response.getSuggestionReports());
                    },
                response -> {
                    getPlayer().sendMessage(ChatColor.RED + "Something went wrong while trying to open the reports menu: " + response.getMessage());
                    Bukkit.getScheduler().runTaskLater(ReportsPlugin.getInstance(), () -> getPlayer().closeInventory(), 1L);
                });
        apiManager.makeRequest(reportsMetaRequest);
    }
}
