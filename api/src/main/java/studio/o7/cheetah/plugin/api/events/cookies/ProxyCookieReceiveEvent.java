package studio.o7.cheetah.plugin.api.events.cookies;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * {@link ProxyCookieReceiveEvent} is fired when a cookie from a client is requested either by a proxy plugin or
 * by a backend server. Cheetah will wait on this event to finish firing before discarding the
 * cookie request (if handled) or forwarding it to the client.
 */
public final class ProxyCookieReceiveEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyCookieReceiveEvent(@NonNull ProxyPlayer player) {
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
