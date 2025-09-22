package de.obey.crown.noobf;

import de.obey.crown.commands.WarpCommand;
import de.obey.crown.core.data.plugin.Log;
import de.obey.crown.core.data.plugin.Messanger;
import de.obey.crown.core.data.plugin.sound.Sounds;
import de.obey.crown.data.WarpHandler;
import de.obey.crown.listener.CoreStart;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public final class CrownWarps extends JavaPlugin {

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
        final PluginManager pluginManager = Bukkit.getPluginManager();
        final WarpCommand warpCommand = new WarpCommand(pluginConfig, messanger, sounds, warpHandler);

        pluginManager.registerEvents(warpCommand, this);

        Objects.requireNonNull(getCommand("warp")).setExecutor(warpCommand);
        Objects.requireNonNull(getCommand("warps")).setExecutor(warpCommand);
        Objects.requireNonNull(getCommand("warp")).setTabCompleter(warpCommand);
    }
}
