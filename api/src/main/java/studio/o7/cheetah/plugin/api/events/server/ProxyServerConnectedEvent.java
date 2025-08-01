package studio.o7.cheetah.plugin.api.events.server;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * {@link ProxyServerConnectedEvent} is fired before the player completely transitions
 * to the target server and the connection to the previous server has been
 * de-established.
 * Use Server to get the target server since {@link ProxyPlayer#getCurrentServer} is yet Empty or
 * listen for {@link ProxyServerPostConnectEvent} instead.
 */
public final class ProxyServerConnectedEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyServerConnectedEvent(@NonNull ProxyPlayer player) {
        super(player);
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
