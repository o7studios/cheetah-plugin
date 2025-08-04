package studio.o7.cheetah.plugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.o7.cheetah.plugin.api.events.block.ProxyBlockEvent;
import studio.o7.octopus.plugin.api.events.OctopusEntryMutationEvent;

import java.time.Duration;
import java.util.UUID;

public final class EntryMutation implements Listener {

    @EventHandler
    public void onEntryMutationEvent(OctopusEntryMutationEvent event) {
        var key = event.getKey();
        var mutation = event.getEntryMutation();

        var data = event.getEntryMutation().getData().getStructValue();

        if (key.startsWith("cheetah::block::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());
            var cluster = cheetah.getClusters().getById(data.get("cluster").getStringValue()).orElseThrow();
            var duration = Duration.ofSeconds((long) data.get("duration_sec").getNumberValue());

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                pm.callEvent(new ProxyBlockEvent(player, cluster, false, duration));
            });
            return;
        }
    }
}
