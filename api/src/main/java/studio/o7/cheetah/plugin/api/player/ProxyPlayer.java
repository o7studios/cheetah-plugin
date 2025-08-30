package studio.o7.cheetah.plugin.api.player;

import com.google.gson.*;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.bedrock.Device;
import studio.o7.cheetah.plugin.api.server.ProxyServer;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a unique Minecraft player, either Java Edition or Bedrock Edition,
 * who may be connected to a {@link ProxyCluster} and {@link ProxyServer}.
 * <p>
 * Instances of this interface may remain accessible even after the underlying player
 * has disconnected. For persistent references it is recommended to use the unique
 * identifier returned by {@link #getUniqueId()} (for Java Edition or converted XUID)
 * or {@link #getXboxUniqueId()} (for Bedrock Edition players).
 * </p>
 */
public interface ProxyPlayer {

    /**
     * Disconnects the player from the proxy with the given reason.
     *
     * @param reason the disconnect reason as a {@link Component}
     * @return {@code true} if the disconnect command was successfully sent, {@code false} otherwise
     */
    boolean disconnect(@NonNull Component reason);

    /**
     * Attempts to connect the player to the given {@link ProxyServer}.
     *
     * @param server the target server
     * @return an optional connection status
     */
    Optional<ConnectionStatus> send(@NonNull ProxyServer server);

    /**
     * Returns the player's current username.
     *
     * @return the username
     */
    String getUsername();

    /**
     * Returns the player's unique identifier.
     * <p>
     * For Java Edition players this is the standard UUID. For Bedrock Edition players,
     * this is a converted UUID based on the Xbox Unique ID (XUID).
     * If you need a stable identifier for Bedrock Edition players, it is recommended to
     * use {@link #getXboxUniqueId()} instead.
     * </p>
     *
     * @return the player's unique identifier
     */
    UUID getUniqueId();

    /**
     * Returns the Xbox Unique ID (XUID) of a Bedrock Edition player.
     * For Java Edition players this value is empty.
     *
     * @return an optional Xbox Unique ID
     */
    Optional<Long> getXboxUniqueId();

    /**
     * Returns the most recent {@link Device} the Bedrock player has been playing on.
     * For Java Edition players this value is empty.
     *
     * @return an optional device
     */
    Optional<Device> getDevice();

    /**
     * Returns whether this player is a Bedrock Edition player.
     *
     * @return {@code true} if Bedrock, {@code false} if Java Edition
     */
    boolean isBedrockPlayer();

    /**
     * Returns the current {@link ProxyServer} the player is connected to.
     * If the player is offline or still connecting, this value is empty.
     *
     * @return an optional server
     */
    Optional<ProxyServer> getCurrentServer();

    /**
     * Returns the current {@link ProxyCluster} the player is connected to.
     * If the player is offline or still connecting, this value is empty.
     *
     * @return an optional cluster
     */
    Optional<ProxyCluster> getCurrentCluster();

    /**
     * Returns all currently active {@link Blockage Blockages} affecting this player
     * across all clusters.
     *
     * @return a collection of blockages
     */
    Collection<Blockage> getBlockages();

    /**
     * Gson adapter for serializing and deserializing {@link ProxyPlayer} instances.
     * <p>
     * Serialization produces a JSON object with {@code uuid}, {@code name}, and
     * optionally {@code xuid}.
     * Deserialization resolves players by UUID, then name, then Xbox Unique ID.
     * </p>
     */
    final class Adapter implements JsonSerializer<ProxyPlayer>, JsonDeserializer<ProxyPlayer> {

        @Override
        public ProxyPlayer deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var object = json.getAsJsonObject();

            if (object.has("uuid")) {
                var uuid = UUID.fromString(object.get("uuid").getAsString());
                var player = Cheetah.get().getPlayers().getByUniqueId(uuid).orElse(null);
                if (player != null) return player;
            }
            if (object.has("name")) {
                var player = Cheetah.get().getPlayers().getByName(object.get("name").getAsString()).orElse(null);
                if (player != null) return player;
            }
            if (object.has("xuid")) {
                return Cheetah.get().getPlayers().getByXboxUniqueId(object.get("xuid").getAsLong()).orElse(null);
            }
            return null;
        }

        @Override
        public JsonElement serialize(ProxyPlayer player, Type type, JsonSerializationContext jsonSerializationContext) {
            var object = new JsonObject();
            object.addProperty("uuid", player.getUniqueId().toString());
            object.addProperty("name", player.getUsername());
            player.getXboxUniqueId().ifPresent(xuid -> object.addProperty("xuid", xuid));
            return object;
        }
    }
}
