/* CrownPlugins - CrownWarps */
/* 30.07.2024 - 21:16 */

package de.obey.crown.data;

import de.obey.crown.core.util.FileUtil;
import de.obey.crown.noobf.CrownWarps;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

@Getter @Setter
public final class Warp {

    private final String name;
    private String prefix, permission;
    private Material material;
    private int slot;

    public Warp(final String name) {
        this.name = name;
        prefix = "&f&l" + name;
        material = Material.STICK;
        slot = 0;
    }

    public Warp saveWarp() {
        final File file = FileUtil.getCreatedFile(CrownWarps.getInstance(), "warps/" + name + ".yml", true);
        final YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        configuration.set("prefix", prefix);
        configuration.set("slot", slot);
        configuration.set("showMaterial", material.name());

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
