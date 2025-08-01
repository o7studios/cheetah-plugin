package studio.o7.cheetah.plugin.api.events.server;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * {@link ProxyServerPostConnectEvent} is fired after the player has connected to a server.
 * The server the player is now connected to is available in {@link ProxyPlayer#getCurrentServer}.
 */
public final class ProxyServerPostConnectEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyServerPostConnectEvent(@NonNull ProxyPlayer player) {
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
