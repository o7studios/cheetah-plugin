package studio.o7.cheetah.plugin;

import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyClusterCollection;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.cluster.ClusterCollectionImpl;
import studio.o7.cheetah.plugin.player.PlayerCollectionImpl;
import studio.o7.cheetah.plugin.server.ServerCollectionImpl;

public class CheetahImpl implements Cheetah {
    public static final PlayerCollectionImpl PLAYER_COLLECTION = new PlayerCollectionImpl();
    public static final ServerCollectionImpl SERVER_COLLECTION = new ServerCollectionImpl();
    public static final ClusterCollectionImpl CLUSTER_COLLECTION = new ClusterCollectionImpl();

    @Override
    public ProxyPlayerCollection getPlayers() {
        return PLAYER_COLLECTION;
    }

    @Override
    public ProxyServerCollection getServers() {
        return SERVER_COLLECTION;
    }

    @Override
    public ProxyClusterCollection getClusters() {
        return CLUSTER_COLLECTION;
    }
}
