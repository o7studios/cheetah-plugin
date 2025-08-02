package studio.o7.cheetah.plugin.api.events.block;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

import java.time.Duration;

/**
 * {@link ProxyBlockEvent} is fired when a player gets blocked from
 * a specific {@link ProxyCluster}.
 */
@Getter
@Setter
public final class ProxyBlockEvent extends ProxyPlayerEvent implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    private final ProxyCluster cluster;
    private boolean cancelled;
    /**
     * The duration the player gets blocked from the cluster
     * or null if permanent.
     */
    @Nullable
    private Duration duration;

    public ProxyBlockEvent(@NonNull ProxyPlayer player, @NonNull ProxyCluster cluster, boolean cancelled, @Nullable Duration duration) {
        super(player);
        this.cluster = cluster;
        this.cancelled = cancelled;
        this.duration = duration;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
