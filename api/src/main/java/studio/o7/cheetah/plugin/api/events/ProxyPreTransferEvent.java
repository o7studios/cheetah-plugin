package studio.o7.cheetah.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

import java.net.InetSocketAddress;

/**
 * {@link ProxyPreTransferEvent} is fired before a player is transferred to another host,
 * by the backend server.
 */
@Getter
public final class ProxyPreTransferEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    private final InetSocketAddress originalAddr;

    @NonNull
    private InetSocketAddress targetAddr;

    private boolean denied = true;

    public ProxyPreTransferEvent(@NonNull ProxyPlayer player, @NonNull InetSocketAddress originalAddr, @NonNull InetSocketAddress targetAddr, boolean denied) {
        super(player);
        this.originalAddr = originalAddr;
        this.targetAddr = targetAddr;
        this.denied = denied;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     * Changes the target address the player will be transferred to.
     */
    public void transferTo(@NonNull InetSocketAddress addr) {
        this.targetAddr = addr;
        this.denied = false;
    }

    /**
     * Returns true if the transfer is allowed.
     */
    public boolean allowed() {
        return !this.denied;
    }
}
