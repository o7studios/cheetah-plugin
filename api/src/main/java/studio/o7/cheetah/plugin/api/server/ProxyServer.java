package studio.o7.cheetah.plugin.api.server;

import com.google.gson.*;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * {@link ProxyServer} represents a Minecraft server (possibly inside
 * a cluster).
 * @apiNote An instance of this class might exist even if the server
 * is already dead. It's recommended to only hold references of
 * this {@link ProxyServer} instance by referencing the id from
 * {@link ProxyServer#getId}
 */
public interface ProxyServer {

    String getId();

    Labels getLabels();

    Optional<ProxyCluster> getCluster();

    ProxyPlayerCollection getPlayers();

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
