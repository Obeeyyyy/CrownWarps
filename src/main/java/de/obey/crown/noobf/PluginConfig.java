package de.obey.crown.noobf;

import de.obey.crown.core.data.plugin.CrownConfig;
import de.obey.crown.core.gui.CrownGuiService;
import de.obey.crown.core.gui.GuiLoader;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;

@Getter
public class PluginConfig extends CrownConfig {

    public PluginConfig(@NonNull Plugin plugin) {
        super(plugin);
    }

    @Override
    public void loadConfig() {
        super.loadConfig();
    }
}
