package com.orangecheese.reports.command.reports;

import com.orangecheese.reports.command.IBaseCommandArgument;
import com.orangecheese.reports.core.gui.window.ReportsMainWindow;
import org.bukkit.entity.Player;

public class ReportsBaseCommand implements IBaseCommandArgument {
    @Override
    public boolean execute(Player player, String[] arguments) {
        ReportsMainWindow window = new ReportsMainWindow(player);
        window.open(1);
        return true;
    }

    @Override
    public boolean hasInfiniteArgument() {
        return false;
    }
}
