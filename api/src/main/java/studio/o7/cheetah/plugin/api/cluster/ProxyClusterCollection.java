package studio.o7.cheetah.plugin.api.cluster;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface ProxyClusterCollection extends Collection<ProxyCluster> {

    default Optional<ProxyCluster> getById(@NonNull String id) {
        return stream()
                .filter(cluster -> cluster.getId().equalsIgnoreCase(id))
                .findAny();
    }
}
