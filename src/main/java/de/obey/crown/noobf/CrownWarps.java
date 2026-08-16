package de.obey.crown.noobf;

import de.obey.crown.commands.WarpCommand;
import de.obey.crown.core.data.plugin.Log;
import de.obey.crown.core.data.plugin.Messanger;
import de.obey.crown.core.data.plugin.sound.Sounds;
import de.obey.crown.data.WarpHandler;
import de.obey.crown.listener.CoreStart;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class CrownWarps extends JavaPlugin {

    public static final String WARP_GUI = "CrownWarps:warps";
    public static final Log log = new Log();

    private PluginConfig pluginConfig;
    private Messanger messanger;
    private Sounds sounds;

    private WarpHandler warpHandler;

    public static CrownWarps getInstance() {
        return getPlugin(CrownWarps.class);
    }

    @Override
    public void onLoad() {
        log.setPlugin(this);

        pluginConfig = new PluginConfig(this);
        messanger = pluginConfig.getMessanger();
        sounds = pluginConfig.getSounds();
    }

    @Override
    public void onEnable() {
        warpHandler = new WarpHandler(pluginConfig, messanger, sounds);
        warpHandler.loadWarps();

        getServer().getPluginManager().registerEvents(new CoreStart(this), this);

        initializeBStats();
    }

    private void initializeBStats() {
        new Metrics(this, 27341);
    }

    public void load() {
        warpHandler.registerWarpActions();

        final WarpCommand warpCommand = new WarpCommand(pluginConfig, messanger, sounds, warpHandler);

        getCommand("warp").setExecutor(warpCommand);
        getCommand("warps").setExecutor(warpCommand);
        getCommand("warp").setTabCompleter(warpCommand);
    }
}
