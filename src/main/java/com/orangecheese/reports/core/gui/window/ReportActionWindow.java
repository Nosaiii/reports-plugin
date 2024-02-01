package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.ResolveReportItem;
import com.orangecheese.reports.core.gui.item.TeleportToPlayerItem;
import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.layout.padded.PaddedWindowLayout;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.ReportData;
import org.bukkit.entity.Player;

public class ReportActionWindow<T> extends Window {
    private final ReportData<T> reportData;

    private final ReportItem<T>[] additionalReportItems;

    public ReportActionWindow(Player player, WindowHistory history, ReportData<T> reportData, ReportItem<T>[] additionalReportItems) {
        super(player, "[#" + reportData.getId() + "] Report actions", new PaddedWindowLayout(4, 1), false, history);
        this.reportData = reportData;
        this.additionalReportItems = additionalReportItems;
    }

    @Override
    public void onInitialize(int page) {
        ResolveReportItem resolveReportItem = new ResolveReportItem(this, reportData.getId(), !reportData.isResolved());
        addItem(resolveReportItem);

        if(reportData.getReporterUuid() != null) {
            TeleportToPlayerItem teleportToPlayerItem = new TeleportToPlayerItem(this, reportData.getReporterUuid(), "Teleport to reporter");
            addItem(teleportToPlayerItem);
        }
    }
}
