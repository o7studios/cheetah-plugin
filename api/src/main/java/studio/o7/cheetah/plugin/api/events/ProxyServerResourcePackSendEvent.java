package studio.o7.cheetah.plugin.api.events;

import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

/**
 * {@link ProxyServerResourcePackSendEvent} is fired when the downstream server tries to send a player a ResourcePack packet.
 * The proxy will wait on this event to finish before forwarding the resource pack to the user.
 * If this event is denied, it will retroactively send a DENIED status to the downstream server in response.
 * If the downstream server has it set to "forced" it will forcefully disconnect the user.
 */
public final class ProxyServerResourcePackSendEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public ProxyServerResourcePackSendEvent(@NonNull ProxyPlayer player) {
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
