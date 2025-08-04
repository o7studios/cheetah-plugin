package studio.o7.cheetah.plugin.api.events.server;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

/**
 * Fired when a player is kicked from a server. You may either allow the proxy to kick the player
 * (with an optional reason override) or redirect the player to a separate server. By default,
 * the proxy will notify the user (if they are already connected to a server) or disconnect them
 * (if they are not on a server and no other servers are available).
 */
@Getter
public final class ProxyKickedFromServerEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    private final ProxyServer server;
    @Nullable
    private final Component originalReason;
    private final boolean duringServerConnect;


    public ProxyKickedFromServerEvent(@NonNull ProxyPlayer player, @NonNull ProxyServer server, @Nullable Component originalReason, boolean duringServerConnect) {
        super(player);
        this.server = server;
        this.originalReason = originalReason;
        this.duringServerConnect = duringServerConnect;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
