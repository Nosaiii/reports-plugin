package com.orangecheese.reports.core.gui.layout.fixed;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.layout.IWindowLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class FixedWindowLayout implements IWindowLayout {
    private final int rows;

    public FixedWindowLayout(int rows) {
        this.rows = rows;
    }

    @Override
    public HashMap<Integer, WindowItem> map(WindowItem[] items) {
        return IntStream.range(0, Math.min(items.length, rows * 9))
                .collect(
                        HashMap::new,
                        (map, index) -> map.put(index, items[index]),
                        Map::putAll
                );
    }

    @Override
    public int getLayoutSize(HashMap<Integer, WindowItem> items) {
        return rows * 9;
    }
}