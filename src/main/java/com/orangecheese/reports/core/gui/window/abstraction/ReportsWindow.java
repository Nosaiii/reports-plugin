package com.orangecheese.reports.core.gui.window.abstraction;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.WindowFilterItem;
import com.orangecheese.reports.core.gui.layout.fixed.FixedWindowLayout;
import com.orangecheese.reports.core.http.APIManager;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class ReportsWindow extends Window implements IFilterWindow {
    protected final APIManager apiManager;

    protected final WindowFilterItem resolvedFilter;

    protected final WindowFilterItem unresolvedFilter;

    public ReportsWindow(Player player, String title, WindowHistory history) {
        super(player, title, new FixedWindowLayout(3), true, history);
        this.apiManager = ServiceContainer.get(APIManager.class);

        resolvedFilter = new WindowFilterItem(this, "Show resolved");
        unresolvedFilter = new WindowFilterItem(this, "Show unresolved");
    }

    @Override
    public void onInitialize(int page) { }

    @Override
    public List<WindowFilterItem> buildFilters() {
        return Arrays.asList(resolvedFilter, unresolvedFilter);
    }
}
