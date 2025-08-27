package studio.o7.cheetah.plugin.api.events.server;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

/**
 * Fired when a player is kicked from a server.
 */
@Getter
public final class ProxyKickedFromServerEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    private final ProxyServer server;
    @SerializedName("during_server_connect")
    private final boolean duringServerConnect;


    public ProxyKickedFromServerEvent(@NonNull ProxyPlayer player, @NonNull ProxyServer server, boolean duringServerConnect) {
        super(player);
        this.server = server;
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
