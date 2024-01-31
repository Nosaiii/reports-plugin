package com.orangecheese.reports.core.gui.data;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class ReportAction {
    private final String message;

    private final BiConsumer<ReportItem, Player> onClick;

    public ReportAction(String message, BiConsumer<ReportItem, Player> onClick) {
        this.message = message;
        this.onClick = onClick;
    }

    public String getMessage() {
        return message;
    }

    public void invoke(ReportItem report, Player player) {
        onClick.accept(report, player);
    }
}