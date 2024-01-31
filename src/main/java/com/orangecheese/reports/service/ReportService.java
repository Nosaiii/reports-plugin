package com.orangecheese.reports.service;

import com.orangecheese.reports.binding.ServiceConstructor;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.ResolveReportRequest;
import com.orangecheese.reports.core.io.ContainerCache;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class ReportService {
    private final APIManager apiManager;

    private final ContainerCache containerCache;

    @ServiceConstructor
    public ReportService(APIManager apiManager, ContainerCache containerCache) {
        this.apiManager = apiManager;
        this.containerCache = containerCache;
    }

    public void resolve(Player player, int id, boolean resolved, Runnable onSuccess) {
        ResolveReportRequest request = new ResolveReportRequest(
                containerCache.getAccessToken(),
                id,
                resolved,
                onSuccess,
                message -> {
                    player.closeInventory();
                    player.sendMessage(ChatColor.RED + "Failed to resolve the report: " + message.getMessage());
                });
        apiManager.makeRequest(request);
    }
}