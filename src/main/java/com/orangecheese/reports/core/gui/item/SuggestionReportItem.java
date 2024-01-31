package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class SuggestionReportItem extends ReportItem {
    public SuggestionReportItem(Window context, int id, UUID reporterUuid, String message, boolean resolved, Date submissionDate) {
        super(context, id, reporterUuid, message, resolved, submissionDate);
    }

    @Override
    public ItemStack update(Player player) {
        return buildInitial(player);
    }
}