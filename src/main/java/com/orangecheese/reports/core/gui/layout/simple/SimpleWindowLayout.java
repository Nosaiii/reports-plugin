package com.orangecheese.reports.core.gui.layout.simple;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.layout.IWindowLayout;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class SimpleWindowLayout implements IWindowLayout {
    private final SimpleWindowLayoutAlignment alignment;

    public SimpleWindowLayout(SimpleWindowLayoutAlignment alignment) {
        this.alignment = alignment;
    }

    public HashMap<Integer, WindowItem> map(WindowItem[] items) {
        return IntStream.range(0, items.length)
                .collect(
                        HashMap::new,
                        (map, index) -> {
                            int slotIndex = index;
                            if(alignment == SimpleWindowLayoutAlignment.RIGHT)
                                slotIndex = 8 - index;

                            map.put(slotIndex, items[index]);
                        },
                        Map::putAll
                );
    }

    public int getLayoutSize(HashMap<Integer, WindowItem> items) {
        if(items.isEmpty())
            return 9;

        int highestSlot = Collections.max(items.keySet());

        if(highestSlot <= 0)
            return 9;

        int rows = (int) Math.ceil((double) (highestSlot + 1) / 9);
        return rows * 9;
    }
}