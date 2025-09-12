package studio.o7.cheetah.plugin;

import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyClusterCollection;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.cluster.ClusterCollectionImpl;
import studio.o7.cheetah.plugin.player.PlayerCollectionImpl;
import studio.o7.cheetah.plugin.server.ServerCollectionImpl;

public final class CheetahImpl implements Cheetah {
    public static final PlayerCollectionImpl ONLINE_PLAYERS = new PlayerCollectionImpl();
    public static final ServerCollectionImpl SERVERS = new ServerCollectionImpl();
    public static final ClusterCollectionImpl CLUSTERS = new ClusterCollectionImpl();

    @Override
    public ProxyPlayerCollection getPlayers() {
        return ONLINE_PLAYERS;
    }

    @Override
    public ProxyServerCollection getServers() {
        return SERVERS;
    }

    @Override
    public ProxyClusterCollection getClusters() {
        return CLUSTERS;
    }
}
