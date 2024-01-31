package com.orangecheese.reports.core.gui.window.abstraction;

import com.orangecheese.reports.core.gui.item.WindowFilterItem;

import java.util.List;

public interface IFilterWindow {
    List<WindowFilterItem> buildFilters();
}