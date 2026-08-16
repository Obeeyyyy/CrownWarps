/* CrownPlugins - CrownWarps */
/* 30.07.2024 - 21:16 */

package de.obey.crown.data;

import de.obey.crown.core.util.FileUtil;
import de.obey.crown.noobf.CrownWarps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter @Setter
public final class Warp {

    private final String name;
    private String prefix, permission;

    public Warp(final String name) {
        this.name = name;
        prefix = "&f&l" + name;
    }

    public Warp saveWarp() {
        final File file = FileUtil.getCreatedFile(CrownWarps.getInstance(), "warps/" + name + ".yml", true);
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("prefix", prefix);

        if(permission != null) {
            configuration.set("permission", permission);
        }

        FileUtil.saveConfigurationIntoFile(configuration, file);
        return this;
    }

    public File getFile() {
        return FileUtil.getCreatedFile(CrownWarps.getInstance(), "warps/" + name + ".yml", false);
    }

}
