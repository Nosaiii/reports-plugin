package com.orangecheese.reports.core.gui.window.abstraction;

import com.orangecheese.reports.ReportsPlugin;
import com.orangecheese.reports.config.MenuConfig;
import com.orangecheese.reports.core.gui.data.WindowHistory;
import com.orangecheese.reports.core.gui.data.WindowHistoryEntry;
import com.orangecheese.reports.core.gui.item.FillerItem;
import com.orangecheese.reports.core.gui.item.PaginationButton;
import com.orangecheese.reports.core.gui.item.WindowFilterItem;
import com.orangecheese.reports.core.gui.item.abstraction.IAutoUpdatingWindowItem;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.layout.IWindowLayout;
import com.orangecheese.reports.event.WindowCloseEvent;
import com.orangecheese.reports.event.WindowItemClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Window {
    private final Player player;

    private final String title;

    private final IWindowLayout layout;

    private final boolean paginated;

    private final WindowHistory history;

    private final ArrayList<WindowItem> items;

    private HashMap<Integer, WindowItem> slottedItems;

    private Inventory inventory;

    private BukkitTask windowUpdateTask;

    private Listener windowCloseEvent;

    private Listener itemClickEvent;

    private int page;

    private final HashMap<String, PaginationButton> paginationButtons;

    private List<WindowFilterItem> filterItems;

    public static final String NEXT_BUTTON_KEY = "PAGINATION_NEXT_BUTTON";

    public static final String PREVIOUS_BUTTON_KEY = "PAGINATION_PREVIOUS_BUTTON";

    public Window(Player player, String title, IWindowLayout layout, boolean paginated, WindowHistory history) {
        this.player = player;
        this.title = title;
        this.layout = layout;
        this.paginated = paginated;
        this.history = history;

        items = new ArrayList<>();

        page = 1;
        paginationButtons = new HashMap<>();

        filterItems = new ArrayList<>();
    }

    public abstract void onInitialize(int page);

    public void open(int page) {
        refresh(page);
        history.add(new WindowHistoryEntry(this, page));
    }

    public void refresh(int page) {
        this.page = page;

        reinitialize();

        player.openInventory(inventory);

        ReportsPlugin plugin = ReportsPlugin.getInstance();

        windowCloseEvent = new WindowCloseEvent(this);
        Bukkit.getServer().getPluginManager().registerEvents(windowCloseEvent, plugin);

        int TICK_PERIOD = 10;
        windowUpdateTask = Bukkit.getScheduler().runTaskTimer(
                plugin,
                this::tick,
                0L,
                TICK_PERIOD
        );
    }

    public void reinitialize() {
        dispose();

        items.clear();
        if(slottedItems != null)
            slottedItems.clear();

        onInitialize(page);
        build();
    }

    public void navigateBack() {
        history.navigateBack(player);
    }

    public void dispose() {
        if(windowUpdateTask != null) {
            windowUpdateTask.cancel();
            windowUpdateTask = null;
        }

        HandlerList.unregisterAll(windowCloseEvent);
        HandlerList.unregisterAll(itemClickEvent);
    }

    public void addItem(WindowItem item) {
        items.add(item);
    }

    public void addFilter(WindowFilterItem filterItem) {
        filterItems.add(filterItem);
    }

    public void notifyUpdate(WindowItem item) {
        int[] slots = slottedItems.
                entrySet()
                .stream()
                .filter(entry -> entry.getValue() == item)
                .mapToInt(Map.Entry::getKey)
                .toArray();

        for(int slot : slots) {
            inventory.setItem(slot, item.getItemStack());
        }
    }

    public void setPage(int page) {
        open(page);
    }

    private void build() {
        MenuConfig menuConfig = ReportsPlugin.getInstance().getReportsConfig().getMenu();

        if(this instanceof IFilterWindow filterWindow)
            filterItems = filterWindow.buildFilters();

        slottedItems = layout.map(getItems());

        int originalInventorySize = layout.getLayoutSize(slottedItems);

        if(menuConfig.isUseFilters())
            buildFilters(originalInventorySize);

        int inventorySize = originalInventorySize;
        if(!filterItems.isEmpty() && menuConfig.isUseFilters())
            inventorySize += 9;
        if(paginated)
            inventorySize += 9;

        String title = menuConfig.getTitleColor() + this.title;
        if(paginated && menuConfig.isShowPageNumber())
            title += " (" + page + ")";
        inventory = Bukkit.createInventory(null, inventorySize, title);

        if(paginated) {
            PaginationButton nextButton = new PaginationButton(this, page + 1);
            inventory.setItem(inventorySize - 1, nextButton.buildItem(NEXT_BUTTON_KEY));
            paginationButtons.put(NEXT_BUTTON_KEY, nextButton);

            if(page > 1) {
                PaginationButton previousButton = new PaginationButton(this, Math.max(page - 1, 1));
                inventory.setItem(inventorySize - 9, previousButton.buildItem(PREVIOUS_BUTTON_KEY));
                paginationButtons.put(PREVIOUS_BUTTON_KEY, previousButton);
            }
        }

        for(Map.Entry<Integer, WindowItem> itemEntry : slottedItems.entrySet()) {
            int slot = itemEntry.getKey();
            WindowItem item = itemEntry.getValue();

            ItemStack itemStack = item.buildInitial(player);
            inventory.setItem(slot, itemStack);
        }

        itemClickEvent = new WindowItemClickEvent(this, slottedItems);
        ReportsPlugin plugin = ReportsPlugin.getInstance();
        Bukkit.getServer().getPluginManager().registerEvents(itemClickEvent, plugin);
    }

    private void buildFilters(int originalInventorySize) {
        if(!filterItems.isEmpty()) {
            for(int i = 0; i < 9; i++) {
                int slot = originalInventorySize + i;

                if(i >= filterItems.size()) {
                    slottedItems.put(slot, new FillerItem(this));
                    continue;
                }

                slottedItems.put(slot, filterItems.get(i));
            }
        }
    }

    private void tick() {
        slottedItems = layout.map(getItems());

        for(Map.Entry<Integer, WindowItem> itemEntry : slottedItems.entrySet()) {
            int slot = itemEntry.getKey();
            WindowItem item = itemEntry.getValue();

            if(item instanceof IAutoUpdatingWindowItem updatingItem) {
                inventory.setItem(slot, updatingItem.update(player));
                continue;
            }

            inventory.setItem(slot, item.getItemStack());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public int getPage() {
        return page;
    }

    public PaginationButton getPaginationButton(String key) {
        if(!paginationButtons.containsKey(key))
            return null;
        return paginationButtons.get(key);
    }

    public WindowHistory getHistory() {
        return history;
    }

    public WindowItem[] getItems() {
        return items.toArray(new WindowItem[0]);
    }

    public Inventory getInventory() {
        return inventory;
    }
}