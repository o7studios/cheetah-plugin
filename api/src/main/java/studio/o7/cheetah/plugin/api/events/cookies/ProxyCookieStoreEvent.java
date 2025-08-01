package studio.o7.cheetah.plugin.api.events.cookies;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * {@link ProxyCookieStoreEvent} is fired when a cookie should be stored on a player's client. This process can be
 * initiated either by a proxy plugin or by a backend server. Cheetah will wait on this event
 * to finish firing before discarding the cookie (if handled) or forwarding it to the client so
 * that it can store the cookie.
 */
public final class ProxyCookieStoreEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyCookieStoreEvent(@NonNull ProxyPlayer player) {
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
