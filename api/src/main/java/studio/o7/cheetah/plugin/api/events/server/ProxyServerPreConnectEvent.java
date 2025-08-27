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

import java.util.Optional;

/**
 * {@link ProxyServerPreConnectEvent} is fired before the player connects to a server.
 */
@Getter
public final class ProxyServerPreConnectEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    @SerializedName("original_server")
    private final ProxyServer original;

    @Nullable
    @SerializedName("previous_server")
    private final ProxyServer previousServer;

    @Nullable
    private ProxyServer server;

    public ProxyServerPreConnectEvent(@NonNull ProxyPlayer player, @NonNull ProxyServer original, @Nullable ProxyServer previousServer, @Nullable ProxyServer server) {
        super(player);
        this.original = original;
        this.previousServer = previousServer;
        this.server = server;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Allow the player to connect to the specified server.
     */
    public void allow(@NonNull ProxyServer server) {
        this.server = server;
    }

    /**
     * Deny will cancel the player to connect to another server.
     */
    public void deny() {
        this.server = null;
    }

    /**
     * Returns true whether the connection is allowed.
     */
    public boolean allowed() {
        return this.server != null;
    }

    /**
     * Returns the server the player will connect to OR
     * Empty if {@link ProxyServerPreConnectEvent#allowed} returns false.
     */
    public Optional<ProxyServer> getServer() {
        return Optional.ofNullable(this.server);
    }

    /**
     * Returns the server the player was previously connected to.
     * May return Empty if there was none!
     */
    public Optional<ProxyServer> getPreviousServer() {
        return Optional.ofNullable(this.previousServer);
    }
}
