package de.obey.crown.noobf;

import de.obey.crown.core.data.plugin.CrownConfig;
import de.obey.crown.core.util.FileUtil;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PluginConfig extends CrownConfig {

    private int guiSize;
    private Material placeholderMaterial;
    private List<Integer> placeholderSlots;

    public PluginConfig(@NonNull Plugin plugin) {
        super(plugin);
    }

    @Override
    public void loadConfig() {

        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(getConfigFile());

        guiSize = FileUtil.getInt(configuration, "gui-size", 27);
        placeholderMaterial = FileUtil.getMaterial(configuration, "placeholder-material", Material.IRON_BARS);
        placeholderSlots = FileUtil.getIntArrayList(configuration, "placeholder-slots", new ArrayList<>());

        if(guiSize % 9 != 0) {
            CrownWarps.log.warn("invalid gui size: " + guiSize);
            CrownWarps.log.warn("defaulting to 27");
            guiSize = 27;
        }

        FileUtil.saveConfigurationIntoFile(configuration, getConfigFile());
    }
}
