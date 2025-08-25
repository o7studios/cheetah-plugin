package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.bedrock.Device;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * {@link ProxyPlayer} represents a real (unique) Minecraft Java or Bedrock Edition
 * player which may be connected to a {@link ProxyCluster} and {@link ProxyServer}
 * @apiNote Instances of this class probably stay consistent, but
 * it's still recommended to only hold references of this {@link ProxyPlayer}
 * instance by referencing the UUID from {@link ProxyPlayer#getUniqueId}
 * or XUID fromm {@link ProxyPlayer#getXboxUniqueId}.
 */
public interface ProxyPlayer {

    boolean disconnect(@NonNull Component reason);

    Optional<ConnectionStatus> send(@NonNull ProxyServer server);

    String getUsername();

    /**
     * Returns the players unique id.
     * If {@link ProxyPlayer} is a Bedrock Edition player ({@link ProxyPlayer#isBedrockPlayer} == true)
     * it returns a converted Xbox Unique id.
     * @apiNote It's recommended to use {@link ProxyPlayer#getXboxUniqueId} on Bedrock Edition players
     * if you intend to create a persistent reference by some unique identifier.
     */
    UUID getUniqueId();

    /**
     * Returns the Bedrock Edition players Xbox Unique id
     * or Empty if the player's on Java Edition.
     */
    Optional<Long> getXboxUniqueId();

    /**
     * The latest device the Bedrock player has been playing on or
     * Empty if the player's on Java Edition.
     */
    Optional<Device> getDevice();

    boolean isBedrockPlayer();

    /**
     * Returns the current {@link ProxyServer} the player is connected to or
     * Empty if the player is offline or still connecting.
     */
    Optional<ProxyServer> getCurrentServer();

    /**
     * Returns the current {@link ProxyCluster} the player is connected to or
     * Empty if the player is offline or still connecting.
     */
    Optional<ProxyCluster> getCurrentCluster();

    /**
     * Returns all currently active blockages
     * from clusters of this {@link ProxyPlayer}.
     */
    Collection<Blockage> getBlockages();
}
