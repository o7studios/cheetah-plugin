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
 * {@link ProxyServerPostConnectEvent} is fired after the player has connected to a server.
 * The server the player is now connected to is available in {@link ProxyPlayer#getCurrentServer}.
 */
@Getter
public final class ProxyServerPostConnectEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @Nullable
    @SerializedName("previous_server")
    private final ProxyServer previous;

    public ProxyServerPostConnectEvent(@NonNull ProxyPlayer player, @Nullable ProxyServer previous) {
        super(player);
        this.previous = previous;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
