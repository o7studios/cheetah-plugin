package studio.o7.cheetah.plugin.cluster;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.Blockage;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.util.Optional;

@RequiredArgsConstructor
public final class ClusterImpl implements ProxyCluster {
    private final String id;


    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Labels getLabels() {
        return null;
    }

    @Override
    public ProxyServerCollection getServers() {
        return null;
    }

    @Override
    public ProxyPlayerCollection getPlayers() {
        return null;
    }

    @Override
    public Optional<Blockage> getBlock(@NonNull String id) {
        return Optional.empty();
    }
}
