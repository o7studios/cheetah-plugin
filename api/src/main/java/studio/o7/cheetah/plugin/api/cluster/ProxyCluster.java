package studio.o7.cheetah.plugin.api.cluster;

import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

public interface ProxyCluster {

    String getId();

    Labels getLabels();

    ProxyServerCollection getServers();

    ProxyPlayerCollection getPlayers();
}
