package studio.o7.cheetah.plugin.api.cluster;

import lombok.NonNull;
import studio.o7.cheetah.plugin.api.player.Blockage;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.util.Optional;

/**
 * {@link ProxyCluster} represents a namespace of Cheetah instances
 * and associated Minecraft servers on a Kubernetes cluster.
 * @apiNote An instance of this class might exist even if the cluster
 * is already dead. It's recommended to only hold references
 * of this {@link ProxyCluster} instance by referencing
 * the id from {@link ProxyCluster#getId}.
 */
public interface ProxyCluster {

    String getId();

    Labels getLabels();

    ProxyServerCollection getServers();

    ProxyPlayerCollection getPlayers();

    Optional<Blockage> getBlock(@NonNull String id);
}
