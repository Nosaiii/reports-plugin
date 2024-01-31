package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.ReportItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class BugReportItem extends ReportItem {
    public BugReportItem(Window context, int id, UUID reporterUuid, String message, boolean resolved, Date submissionDate, String stepsToReproduce) {
        super(context, id, reporterUuid, message, resolved, submissionDate);
        addAdditionalArgument("Steps to reproduce", stepsToReproduce);
    }

    @Override
    public ItemStack update(Player player) {
        return buildInitial(player);
    }
}