package com.orangecheese.reports.core.gui.window;

import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.DeleteReportItem;
import com.orangecheese.reports.core.gui.item.ResolveReportItem;
import com.orangecheese.reports.core.gui.item.TeleportToPlayerItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.layout.padded.PaddedWindowLayout;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.core.http.response.ReportData;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReportActionWindow<T> extends Window {
    private final ReportData<T> reportData;

    private final List<WindowItem> additionalOptions;

    public ReportActionWindow(Player player, WindowHistory history, ReportData<T> reportData) {
        super(player, "[#" + reportData.getId() + "] Report actions", new PaddedWindowLayout(4, 1), false, history);
        this.reportData = reportData;
        additionalOptions = new ArrayList<>();
    }

    @Override
    public void onInitialize(int page) {
        ResolveReportItem resolveReportItem = new ResolveReportItem(this, reportData.getId(), !reportData.isResolved());
        addItem(resolveReportItem);

        DeleteReportItem deleteReportItem = new DeleteReportItem(this, reportData.getId());
        addItem(deleteReportItem);

        if (reportData.getReporterUuid() != null) {
            TeleportToPlayerItem teleportToPlayerItem = new TeleportToPlayerItem(this, reportData.getReporterUuid(), "Teleport to reporter");
            addItem(teleportToPlayerItem);
        }

        for(WindowItem additionalOption : additionalOptions)
            addItem(additionalOption);
    }

    public void addAdditionalOption(WindowItem item) {
        additionalOptions.add(item);
    }
}