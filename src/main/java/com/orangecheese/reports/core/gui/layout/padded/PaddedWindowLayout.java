package com.orangecheese.reports.core.gui.layout.padded;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.layout.IWindowLayout;

import java.util.Arrays;
import java.util.HashMap;

public class PaddedWindowLayout implements IWindowLayout {
    private final int itemsPerRow;

    private final int verticalPadding;

    public PaddedWindowLayout(int itemsPerRow, int verticalPadding) {
        this.itemsPerRow = itemsPerRow;
        this.verticalPadding = verticalPadding;
    }

    public HashMap<Integer, WindowItem> map(WindowItem[] items) {
        HashMap<Integer, WindowItem> mappedItems = new HashMap<>();

        for(int row = 0; row < getRowsByItemAmount(items.length); row++) {
            WindowItem[] itemsForRow = Arrays.stream(items)
                    .skip((long) row * itemsPerRow)
                    .limit(itemsPerRow)
                    .toArray(WindowItem[]::new);

            int slotOffset = row * 9 * (verticalPadding + 1) + (verticalPadding * 9);

            int startIndex = 4;
            if(itemsForRow.length > 1) {
                int centerIndex = startIndex;
                int subtractionOffset = (int) (itemsForRow.length % 2 == 0 ?
                        Math.ceil((itemsForRow.length + 1) / 2.0d) :
                        Math.floor((itemsForRow.length + 1) / 2.0d));
                startIndex = centerIndex - subtractionOffset;
            }

            for(int i = 0; i < itemsForRow.length; i++) {
                WindowItem item = itemsForRow[i];
                int paddedSlot = startIndex + (i * 2);
                int absoluteSlot = paddedSlot + slotOffset;
                mappedItems.put(absoluteSlot, item);
            }
        }

        return mappedItems;
    }

    public int getLayoutSize(HashMap<Integer, WindowItem> items) {
        int rows = getRowsByItemAmount(items.size());
        return rows * 9 + (verticalPadding * (rows + 1) * 9);
    }

    private int getRowsByItemAmount(int items) {
        return (int) Math.ceil((double) items / itemsPerRow);
    }
}
