package studio.o7.cheetah.plugin.api.player;

import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.bedrock.Device;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

import java.util.Optional;
import java.util.UUID;

public interface ProxyPlayer {

    String getUsername();

    UUID getUniqueId();

    Optional<String> getXboxUniqueId();

    Optional<Device> getDevice();

    boolean isBedrockPlayer();

    Optional<ProxyServer> getCurrentServer();

    Optional<ProxyCluster> getConnectedCluster();
}
