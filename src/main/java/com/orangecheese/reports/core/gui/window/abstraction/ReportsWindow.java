package com.orangecheese.reports.core.gui.window.abstraction;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.item.WindowFilterItem;
import com.orangecheese.reports.core.gui.layout.fixed.FixedWindowLayout;
import com.orangecheese.reports.core.http.APIManager;
import com.orangecheese.reports.core.http.request.report.ReadReportsRequest;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class ReportsWindow<TAttributes, TRequest extends ReadReportsRequest<TAttributes>> extends Window implements IFilterWindow {
    protected final APIManager apiManager;

    protected final WindowFilterItem resolvedFilter;

    protected final WindowFilterItem unresolvedFilter;

    public ReportsWindow(Player player, String title, WindowHistory history) {
        super(player, title, new FixedWindowLayout(3), true, history);
        this.apiManager = ServiceContainer.get(APIManager.class);

        resolvedFilter = new WindowFilterItem(this, "Show resolved");
        unresolvedFilter = new WindowFilterItem(this, "Show unresolved");
    }

    public abstract TRequest prepareRequest(int page, boolean showResolved, boolean showUnresolved);

    @Override
    public void onInitialize(int page) {
        boolean showResolved = resolvedFilter.isActive();
        boolean showUnresolved = resolvedFilter.isActive();

        TRequest readRequest = prepareRequest(page, showResolved, showUnresolved);
        apiManager.makeRequest(readRequest);
    }

    @Override
    public List<WindowFilterItem> buildFilters() {
        return Arrays.asList(resolvedFilter, unresolvedFilter);
    }
}
