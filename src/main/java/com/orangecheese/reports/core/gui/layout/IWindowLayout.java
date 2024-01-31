package com.orangecheese.reports.core.gui.layout;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;

import java.util.HashMap;

public interface IWindowLayout {
    HashMap<Integer, WindowItem> map(WindowItem[] items);

    int getLayoutSize(HashMap<Integer, WindowItem> items);
}