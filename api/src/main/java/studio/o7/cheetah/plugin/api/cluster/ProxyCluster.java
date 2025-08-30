package studio.o7.cheetah.plugin.api.cluster;

import com.google.gson.*;
import lombok.NonNull;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.player.Blockage;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Represents a namespace of Cheetah instances and associated Minecraft servers
 * running on a Kubernetes cluster.
 * <p>
 * A {@code ProxyCluster} object may still be accessible even if the actual cluster
 * no longer exists. For long-term references, it is recommended to store and use
 * the cluster identifier obtained via {@link ProxyCluster#getId()} rather than
 * keeping the {@code ProxyCluster} instance itself.
 * </p>
 */
public interface ProxyCluster {

    /**
     * Returns the unique identifier of this cluster.
     *
     * @return the cluster ID
     */
    String getId();

    /**
     * Returns the labels associated with this cluster.
     *
     * @return the labels
     */
    Labels getLabels();

    /**
     * Returns the collection of proxy servers belonging to this cluster.
     *
     * @return the server collection
     */
    ProxyServerCollection getServers();

    /**
     * Returns the collection of proxy players connected to this cluster.
     *
     * @return the player collection
     */
    ProxyPlayerCollection getPlayers();


    /**
     * Retrieves a blockage definition by its identifier, if present.
     *
     * @param id the blockage identifier
     * @return an optional blockage
     */
    Optional<Blockage> getBlock(@NonNull String id);


    /**
     * Gson adapter for serializing and deserializing {@link ProxyCluster} objects
     * by their identifier.
     */
    final class Adapter implements JsonSerializer<ProxyCluster>, JsonDeserializer<ProxyCluster> {

        @Override
        public ProxyCluster deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            var id = json.getAsString();
            if (id.isEmpty()) return null;
            return Cheetah.get().getClusters().getById(id).orElse(null);
        }

        @Override
        public JsonElement serialize(ProxyCluster cluster, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(cluster.getId());
        }
    }
}
