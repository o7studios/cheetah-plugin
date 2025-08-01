package studio.o7.cheetah.plugin.api;

import studio.o7.cheetah.plugin.api.cluster.ProxyClusterCollection;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;

public interface Cheetah {

    ProxyPlayerCollection getPlayers();

    ProxyServerCollection getServers();

    ProxyClusterCollection getClusters();
}
