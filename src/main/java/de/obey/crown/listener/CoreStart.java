package de.obey.crown.listener;

import de.obey.crown.core.event.CoreStartEvent;
import de.obey.crown.noobf.CrownWarps;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class CoreStart implements Listener {

    private final CrownWarps crownWarps;

    @EventHandler
    public void on(final CoreStartEvent event) {

        crownWarps.setExecutor(event.getCrownCore().getExecutorService());
        event.sendStartupMessage(crownWarps);
        crownWarps.load();
    }

}
