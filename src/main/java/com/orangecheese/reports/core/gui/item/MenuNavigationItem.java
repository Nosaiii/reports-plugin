package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.ChatPaginator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuNavigationItem extends WindowItem {
    private final Window to;

    private final Material material;

    private final String label;

    private String description;

    private String[] additionalLore;

    private final int LINE_LENGTH = 35;

    public MenuNavigationItem(
            Window context,
            Window to,
            String requiredPermission,
            Material material,
            String label,
            String description,
            String... additionalLore) {
        super(context);
        this.to = to;
        this.material = material;
        this.label = label;
        this.description = description;
        this.additionalLore = additionalLore;

        setRequiredPermissions(requiredPermission);

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            if(to == null)
                return;

            if(!hasPermissions(player)) {
                player.sendMessage(ChatColor.RED + "You are not permitted to navigate to the '" + label + "' menu!");
                player.closeInventory();
                return;
            }

            to.open(1);
        });
    }

    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(material);

        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            meta.setDisplayName(ChatColor.GOLD + label);

            List<String> lore = getLore(player);
            meta.setLore(lore);

            item.setItemMeta(meta);
        }

        return item;
    }

    protected List<String> getLore(Player player) {
        List<String> lore = new ArrayList<>();

        if(!description.isEmpty())
            lore.addAll(List.of(ChatPaginator.wordWrap(ChatColor.DARK_GRAY + description, LINE_LENGTH)));

        if(additionalLore.length > 0) {
            lore.add("");
            for(String additionalLoreLine : additionalLore)
                lore.addAll(Arrays.asList(ChatPaginator.wordWrap(ChatColor.RESET + additionalLoreLine, LINE_LENGTH)));
        }

        if(!hasPermissions(player)) {
            lore.add("");
            lore.addAll(Arrays.asList(ChatPaginator.wordWrap(ChatColor.DARK_RED + "You do not have the required permissions to access this menu!", LINE_LENGTH)));
        }

        return lore;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAdditionalLore(String... additionalLore) {
        this.additionalLore = additionalLore;
    }
}
