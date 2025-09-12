package studio.o7.cheetah.plugin.api.events;

import lombok.Getter;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

@Getter
public abstract class ProxyPlayerEvent extends ProxyEvent {
    private final ProxyPlayer player;

    public ProxyPlayerEvent(ProxyPlayer player) {
        this.player = player;
    }
}
