package studio.o7.cheetah.plugin;

import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyClusterCollection;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;

public class CheetahImpl implements Cheetah {

    @Override
    public ProxyPlayerCollection getPlayers() {
        return null;
    }

    @Override
    public ProxyServerCollection getServers() {
        return null;
    }

    @Override
    public ProxyClusterCollection getClusters() {
        return null;
    }
}
