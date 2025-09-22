/* CrownPlugins - CrownEnderchest */
/* 21.07.2024 - 01:13 */

package de.obey.crown.data;

import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public final class WarpHolder implements InventoryHolder {

    @Getter
    private final String name = "crownholder-warps";

    public WarpHolder() {

    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
