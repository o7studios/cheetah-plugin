package studio.o7.cheetah.plugin.api.events.server;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * Fired when a player is kicked from a server. You may either allow the proxy to kick the player
 * (with an optional reason override) or redirect the player to a separate server. By default,
 * the proxy will notify the user (if they are already connected to a server) or disconnect them
 * (if they are not on a server and no other servers are available).
 */
public final class ProxyKickedFromServerEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyKickedFromServerEvent(@NonNull ProxyPlayer player) {
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
