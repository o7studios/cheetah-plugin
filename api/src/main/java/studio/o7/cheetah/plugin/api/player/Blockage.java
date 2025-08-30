package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;

import java.time.Duration;
import java.util.Optional;

/**
 * Represents a block applied to a {@link ProxyPlayer} within a {@link ProxyCluster}.
 * <p>
 * A {@code Blockage} can have a finite duration or be permanent. It may remain
 * accessible even after the block has already been lifted or deleted. For long-term
 * references, it is recommended to store and use the block identifier obtained via
 * {@link Blockage#getId()} instead of holding onto the {@code Blockage} instance itself.
 * </p>
 */
public interface Blockage {

    /**
     * Returns the unique identifier of this block.
     * This identifier is also shown to the player inside the kick message
     * for support purposes.
     *
     * @return the block ID
     */
    String getId();

    /**
     * Returns the remaining duration of this block.
     * If the block is permanent, the result is empty.
     *
     * @return an optional duration
     */
    Optional<Duration> getDuration();

    /**
     * Sets a finite duration for this block.
     * Once the duration has expired, the block is automatically deleted.
     *
     * @param duration the duration until expiry
     */
    void setDuration(@NonNull Duration duration);

    /**
     * Removes the duration from this block, making it permanent.
     */
    void setPermanent();

    /**
     * Returns the internal reason for why the {@link ProxyPlayer}
     * has been blocked from the {@link ProxyCluster}.
     *
     * @return the reason string
     */
    String getReason();

    /**
     * Returns the {@link ProxyPlayer} affected by this block.
     *
     * @return the blocked player
     */
    ProxyPlayer getPlayer();

    /**
     * Returns the {@link ProxyCluster} where the {@link ProxyPlayer} is blocked.
     *
     * @return the cluster
     */
    ProxyCluster getCluster();

    /**
     * Lifts this block, unblocking the player by deleting the {@code Blockage}.
     */
    void delete();
}
