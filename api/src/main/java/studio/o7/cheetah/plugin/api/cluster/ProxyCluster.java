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
