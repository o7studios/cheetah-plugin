package studio.o7.cheetah.plugin.api.server;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

/**
 * Represents a collection of {@link ProxyServer} instances
 * currently known to the proxy system.
 * <p>
 * Provides lookup methods by server identifiers such as
 * the unique server id.
 * </p>
 */
public interface ProxyServerCollection extends Collection<ProxyServer> {

    /**
     * Returns a server by its unique identifier.
     * <p>
     * The identifier is stable within the cluster and can be used
     * to persistently reference a {@link ProxyServer}, even if
     * the underlying server instance is restarted or recreated.
     * </p>
     *
     * @param id the unique server id
     * @return an optional server with the given id
     */
    Optional<ProxyServer> getById(@NonNull String id);
}
