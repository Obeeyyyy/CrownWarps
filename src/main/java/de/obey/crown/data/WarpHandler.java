package de.obey.crown.data;

import com.google.common.collect.Maps;
import de.obey.crown.core.data.plugin.Messanger;
import de.obey.crown.core.data.plugin.sound.Sounds;
import de.obey.crown.core.gui.CrownGuiService;
import de.obey.crown.core.handler.LocationHandler;
import de.obey.crown.core.util.FileUtil;
import de.obey.crown.core.util.Teleporter;
import de.obey.crown.noobf.CrownWarps;
import de.obey.crown.noobf.PluginConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Map;

@RequiredArgsConstructor
public class WarpHandler {

    private final PluginConfig pluginConfig;
    private final Messanger messanger;
    private final Sounds sounds;

    @Getter
    private final Map<String, Warp> warps = Maps.newConcurrentMap();

    public boolean exists(final String warpName) {
        return warps.containsKey(warpName);
    }

    public Warp getWarp(final String warpName) {
        return warps.get(warpName);
    }

    public void loadWarps() {
        final File folder = new File(CrownWarps.getInstance().getDataFolder() + "/warps");

        warps.clear();

        if(!folder.exists())
            folder.mkdir();

        if(folder.listFiles() == null)
            return;

        for (final File file : folder.listFiles()) {
            final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            final String warpName = file.getName().replaceFirst("\\.yml$", "");
            final Warp warp = new Warp(warpName);

            warp.setPrefix(FileUtil.getString(configuration, "prefix", "&f&l" + warpName));
            warp.setPermission(FileUtil.getString(configuration, "permission", null));

            warps.put(warp.getName(), warp);
        }
    }

    public void createWarp(final Player player, String warpName) {
        warpName = warpName.toLowerCase();
        if (warps.containsKey(warpName)) {
            messanger.sendMessage(player, "warp-already-exists", new String[]{"name"}, warpName);
            sounds.playSoundToPlayer(player, "warp-already-exists");
            return;
        }

        warps.put(warpName, new Warp(warpName).saveWarp());
        LocationHandler.setLocation("warp-" + warpName, player.getLocation());
        messanger.sendMessage(player, "warp-created", new String[]{"name"}, warpName);
        sounds.playSoundToPlayer(player, "warp-created");
    }

    public void deleteWarp(final Player player, String warpName) {
        warpName = warpName.toLowerCase();
        if (!warps.containsKey(warpName)) {
            messanger.sendMessage(player, "warp-does-not-exist", new String[]{"name"}, warpName);
            sounds.playSoundToPlayer(player, "warp-does-not-exist");
            return;
        }

        warps.get(warpName).getFile().delete();

        warps.remove(warpName);
        messanger.sendMessage(player, "warp-deleted", new String[]{"name"}, warpName);
        sounds.playSoundToPlayer(player, "warp-deleted");
    }

    public void openWarpInventory(final Player player) {
        CrownGuiService.open(player, CrownWarps.WARP_GUI);
    }

    public void teleportToWarp(final Player player, final String warpName) {
        if(!exists(warpName)) {
            messanger.sendMessage(player, "warp-does-not-exist", new String[]{"name"}, warpName);
            sounds.playSoundToPlayer(player, "warp-does-not-exist");
            return;
        }

        final Warp warp = getWarp(warpName);

        if(warp.getPermission() != null) {
            if(!messanger.hasPermission(player, warp.getPermission()))
                return;
        }

        Teleporter.teleportWithAnimation(player, "warp-" + warpName);
    }

}
