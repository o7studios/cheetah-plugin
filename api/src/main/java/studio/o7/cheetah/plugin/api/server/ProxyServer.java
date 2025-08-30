package studio.o7.cheetah.plugin.api.server;

import com.google.gson.*;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.lang.reflect.Type;
import java.util.Optional;


/**
 * Represents a Minecraft server, which may optionally belong to a {@link ProxyCluster}.
 * <p>
 * A {@code ProxyServer} object may remain accessible even if the underlying server
 * has already shut down or been removed. For long-term references, it is recommended
 * to store and use the server identifier obtained via {@link ProxyServer#getId()}
 * instead of holding onto the {@code ProxyServer} instance itself.
 * </p>
 */
public interface ProxyServer {

    /**
     * Returns the unique identifier of this server.
     *
     * @return the server ID
     */
    String getId();

    /**
     * Returns the labels associated with this server.
     *
     * @return the labels
     */
    Labels getLabels();

    /**
     * Returns the cluster this server belongs to, if any.
     *
     * @return an optional cluster
     */
    Optional<ProxyCluster> getCluster();

    /**
     * Returns the collection of players currently connected to this server.
     *
     * @return the player collection
     */
    ProxyPlayerCollection getPlayers();

    /**
     * Gson adapter for serializing and deserializing {@link ProxyServer} objects
     * by their identifier.
     */
    final class Adapter implements JsonSerializer<ProxyServer>, JsonDeserializer<ProxyServer> {

        @Override
        public ProxyServer deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var id = json.getAsString();
            if (id.isEmpty()) return null;
            return Cheetah.get().getServers().getById(id).orElse(null);
        }

        @Override
        public JsonElement serialize(ProxyServer server, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(server.getId());
        }
    }
}
