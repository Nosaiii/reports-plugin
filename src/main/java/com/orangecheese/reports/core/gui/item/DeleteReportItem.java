package com.orangecheese.reports.core.gui.item;

import com.orangecheese.reports.binding.ServiceContainer;
import com.orangecheese.reports.core.gui.item.abstraction.WindowItem;
import com.orangecheese.reports.core.gui.window.abstraction.Window;
import com.orangecheese.reports.service.ReportService;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DeleteReportItem extends WindowItem {
    private final ReportService reportService;

    public DeleteReportItem(Window context, int id) {
        super(context);

        reportService = ServiceContainer.get(ReportService.class);

        setOnClickListener(ClickType.LEFT, (item, player) -> {
            reportService.delete(player, id, () -> {
                player.sendMessage(ChatColor.GREEN + "You have successfully deleted report #" + id + "!");
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2.0f, 2.0f);
                context.navigateBack();
            });
        });
    }

    @Override
    protected ItemStack renderInitial(Player player) {
        ItemStack item = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta meta = item.getItemMeta();

        if(meta != null) {
            String displayName = ChatColor.RED + "Delete report";
            meta.setDisplayName(displayName);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.DARK_GRAY + "Delete the report.");
            lore.add(ChatColor.DARK_GRAY + "This action can not be undone.");
            meta.setLore(lore);
        }

        item.setItemMeta(meta);

        return item;
    }
}
