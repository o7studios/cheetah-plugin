package studio.o7.cheetah.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.Event;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

@Getter
public abstract class ProxyPlayerEvent extends Event {
    private final ProxyPlayer player;

    public ProxyPlayerEvent(@NonNull ProxyPlayer player) {
        this.player = player;
    }
}
