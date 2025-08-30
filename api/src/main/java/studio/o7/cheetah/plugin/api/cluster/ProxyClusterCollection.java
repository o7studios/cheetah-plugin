package studio.o7.cheetah.plugin.api.cluster;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a collection of {@link ProxyCluster} instances
 * currently known to the proxy system.
 * <p>
 * Provides lookup methods by cluster identifiers such as
 * the unique cluster id.
 * </p>
 */
public interface ProxyClusterCollection extends Collection<ProxyCluster> {

    /**
     * Returns a cluster by its unique identifier.
     * <p>
     * The identifier is stable across the system and can be used
     * to persistently reference a {@link ProxyCluster}, even if
     * the underlying cluster instance is already terminated or recreated.
     * </p>
     *
     * @param id the unique cluster id
     * @return an optional cluster with the given id
     */
    Optional<ProxyCluster> getById(@NonNull String id);
}
