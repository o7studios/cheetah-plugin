package studio.o7.cheetah.plugin.api.server;

import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.util.Optional;

public interface ProxyServer {

    String getId();

    Labels getLabels();

    Optional<ProxyCluster> getCluster();

    ProxyPlayerCollection getPlayers();
}
