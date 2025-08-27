package studio.o7.cheetah.plugin.api.events.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

/**
 * {@link ProxyChooseInitialServerEvent} is fired when a player has finished the login process,
 * and we need to choose the first server to connect to.
 * The proxy will wait on this event to finish firing before initiating the connection,
 * but you should try to limit the work done in this event.
 * Failures will be handled by {@link ProxyKickedFromServerEvent} as normal.
 */
@Getter
@Setter
public final class ProxyChooseInitialServerEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    @SerializedName("initial_server")
    private ProxyServer initialServer;

    public ProxyChooseInitialServerEvent(@NonNull ProxyPlayer player, @NonNull ProxyServer initialServer) {
        super(player);
        this.initialServer = initialServer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
