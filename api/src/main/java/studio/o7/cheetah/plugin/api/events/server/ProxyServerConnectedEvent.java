package studio.o7.cheetah.plugin.api.events.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

/**
 * {@link ProxyServerConnectedEvent} is fired before the player completely transitions
 * to the target server and the connection to the previous server has been
 * de-established.
 * Use Server to get the target server since {@link ProxyPlayer#getCurrentServer} is yet Empty or
 * listen for {@link ProxyServerPostConnectEvent} instead.
 */
@Getter
public final class ProxyServerConnectedEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    @SerializedName("server")
    private final ProxyServer server;
    @Nullable
    @SerializedName("previous_server")
    private final ProxyServer previousServer;

    public ProxyServerConnectedEvent(@NonNull ProxyPlayer player, @NonNull ProxyServer server, @Nullable ProxyServer previousServer) {
        super(player);
        this.server = server;
        this.previousServer = previousServer;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
