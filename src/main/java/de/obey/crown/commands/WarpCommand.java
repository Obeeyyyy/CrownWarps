/* CrownPlugins - CrownWarps */
/* 30.07.2024 - 21:34 */

package de.obey.crown.commands;

import de.obey.crown.core.data.plugin.Messanger;
import de.obey.crown.core.data.plugin.sound.Sounds;
import de.obey.crown.core.util.InventoryUtil;
import de.obey.crown.data.Warp;
import de.obey.crown.data.WarpHandler;
import de.obey.crown.data.WarpHolder;
import de.obey.crown.noobf.CrownWarps;
import de.obey.crown.noobf.PluginConfig;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class WarpCommand implements CommandExecutor, Listener, TabCompleter {

    private final PluginConfig pluginConfig;
    private final Messanger messanger;
    private final Sounds sounds;
    private final WarpHandler warpHandler;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if (!(sender instanceof Player player))
            return false;

        if (command.getName().equalsIgnoreCase("warp")) {

            if (args.length == 0) {
                warpHandler.openWarpInventory(player);
                return false;
            }

            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("reload")) {

                    if (!messanger.hasPermission(player, "command.warp.admin"))
                        return false;

                    pluginConfig.loadConfig();
                    pluginConfig.loadMessages();
                    pluginConfig.loadSounds();

                    messanger.sendMessage(sender, "plugin-reloaded", new String[]{"plugin"}, CrownWarps.getInstance().getName());

                    return false;
                }
            }

            if (args.length == 1) {

                if(args[0].equalsIgnoreCase("list")) {
                    if (!messanger.hasPermission(player, "command.warp.admin")) {
                        return false;
                    }

                    messanger.sendNonConfigMessage(sender, "%prefix% There are " + warpHandler.getWarps().size() + " warp" + (warpHandler.getWarps().size() != 1 ? "s": ""));

                    for (final Warp warp : warpHandler.getWarps().values()) {
                        messanger.sendNonConfigMessage(sender,"- " + warp.getName());
                        messanger.sendNonConfigMessage(sender,"  prefix: " + warp.getPrefix());
                        messanger.sendNonConfigMessage(sender,"  slot: " + warp.getSlot());
                        messanger.sendNonConfigMessage(sender,"  material: " + warp.getMaterial().name());
                    }

                    return false;
                }

                final String warpName = args[0].toLowerCase();
                warpHandler.teleportToWarp(player, warpName);
                return false;
            }

            if (!messanger.hasPermission(player, "command.warp.admin"))
                return false;

            final String warpName = args[1].toLowerCase();

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    warpHandler.createWarp(player, warpName);
                    return false;
                }
            }

            if(!warpHandler.exists(warpName)) {
                messanger.sendMessage(player, "warp-does-not-exist", new String[]{"name"}, warpName);
                sounds.playSoundToPlayer(player, "warp-does-not-exist");
                return false;
            }

            final Warp warp = warpHandler.getWarp(warpName);

            if(args.length == 2) {
                if (args[0].equalsIgnoreCase("delete")) {
                    warpHandler.deleteWarp(player, warpName);
                    return false;
                }

                if (args[0].equalsIgnoreCase("setitem")) {


                    if(!InventoryUtil.hasItemInHand(player)) {
                        messanger.sendMessage(sender, "no-item-in-hand");
                        return false;
                    }

                    messanger.sendNonConfigMessage(sender, "%prefix% You have set the show material for '" + warpName + "'.");

                    warp.setMaterial(player.getInventory().getItemInMainHand().getType());
                    warp.saveWarp();

                    return false;
                }

                if (args[0].equalsIgnoreCase("setlocation")) {
                    Bukkit.dispatchCommand(sender, "location set warp-" + warpName);
                    return false;
                }
            }

            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("setslot")) {

                    final int newSlot = messanger.isValidInt(sender, args[2], -1);

                    if(newSlot < 0) {
                        return false;
                    }

                    messanger.sendNonConfigMessage(sender, "%prefix% You have set slot for '" + warpName + "' to " + newSlot + ".");
                    warp.setSlot(newSlot);

                    return false;
                }
            }

            if (args[0].equalsIgnoreCase("setprefix")) {

                String prefix = args[2];

                for (int i = 3; i < args.length; i++) {
                    prefix = prefix + " " + args[i];
                }

                warp.setPrefix(prefix);
                messanger.sendNonConfigMessage(sender, "%prefix% Set prefix for warp '" + warpName + "' to " + prefix + ".");

                return false;
            }

            messanger.sendCommandSyntax(sender, "/warp",
                    "/warp reload",
                    "/warp create <name>",
                    "/warp delete <name>",
                    "/warp setlocation <name>",
                    "/warp setslot <name> <slot>",
                    "/warp setprefix <name> <prefix>",
                    "/warp setitem <name>"
            );

            return false;
        }

        if (command.getName().equalsIgnoreCase("warps")) {
            if (warpHandler.getWarps().isEmpty()) {
                messanger.sendMessage(player, "no-warps-yet");
                return false;
            }

            messanger.sendMessage(player, "warp-list-pre-line");
            warpHandler.getWarps().keySet().forEach(name -> player.sendMessage("§8 - §f§o" + name));
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if(!(sender instanceof Player))
            return null;

        final ArrayList<String> list = new ArrayList<>();

        if(args.length == 1) {
            list.addAll(warpHandler.getWarps().keySet());

            if(sender.hasPermission("command.warp.admin")) {
                list.add("create");
                list.add("delete");
                list.add("setslot");
                list.add("setitem");
                list.add("setprefix");
                list.add("setlocation");
                list.add("reload");
                list.add("list");
            }
        }

        if(args.length == 2) {
            if(args[0].equalsIgnoreCase("create")) {
                list.add("warp-name");

            } else

            if(sender.hasPermission("command.warp.admin")) {
                list.addAll(warpHandler.getWarps().keySet());
            }
        }

        final String argument = args[args.length - 1];
        if (!argument.isEmpty())
            list.removeIf(value -> !value.toLowerCase().startsWith(argument.toLowerCase()));

        return list;
    }

    @EventHandler
    public void on(final InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player player))
            return;

        if(!(event.getView().getTopInventory().getHolder() instanceof WarpHolder))
            return;

        event.setCancelled(true);

        if(event.getCurrentItem() == null)
            return;

        if(pluginConfig.getPlaceholderSlots().contains(event.getSlot()))
            return;

        for (Warp warp : warpHandler.getWarps().values()) {
            if (warp.getSlot() == event.getSlot()) {
                warpHandler.teleportToWarp((Player) event.getWhoClicked(), warp.getName());
                player.closeInventory();
                break;
            }
        }
    }
}
