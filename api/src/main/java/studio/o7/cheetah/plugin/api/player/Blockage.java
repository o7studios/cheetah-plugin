package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;

import java.time.Duration;
import java.util.Optional;

/**
 * {@link Blockage} represents a blockage of a {@link ProxyPlayer}
 * from a cluster with a possible duration.
 * @apiNote An instance of this class might exist even if the block
 * has already been deleted. It's recommended to only hold references of
 * this {@link Blockage} instance by referencing the
 * id from {@link Blockage#getId}
 */
public interface Blockage {

    /**
     * A unique id to this block which is shown
     * to the player inside the kick message for
     * support purposes.
     */
    String getId();

    /**
     * Returns the remaining duration of this block or
     * Empty if permanent.
     */
    Optional<Duration> getDuration();

    /**
     * Sets the duration of this block.
     * After this duration expired, the block is automatically deleted.
     */
    void setDuration(@NonNull Duration duration);

    /**
     * Removes the duration of this block and makes
     * it permanent.
     */
    void setPermanent();

    /**
     * An intern reason for why the {@link ProxyPlayer}
     * has been blocked from a {@link ProxyCluster}
     */
    String getReason();

    /**
     * Returns the {@link ProxyPlayer} who has
     * been blocked.
     */
    ProxyPlayer getPlayer();

    /**
     * Returns the {@link ProxyCluster} the {@link ProxyPlayer}
     * has been blocked on.
     */
    ProxyCluster getCluster();

    /**
     * Unblocks the player by deleting this blockage.
     */
    void delete();
}
