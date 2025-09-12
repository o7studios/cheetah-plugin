package studio.o7.cheetah.plugin.server;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Struct;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.cheetah.plugin.api.utils.Labels;
import studio.o7.cheetah.plugin.player.PlayerCollectionImpl;
import studio.o7.octopus.sdk.structs.ProtoStruct;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class ServerImpl implements ProxyServer {
    private final String id;
    private Labels labels;
    private String clusterId;

    @AllArgsConstructor
    private static final class ServerStruct extends ProtoStruct {
        private final String id;
        private final Map<String, String> labels;
        @SerializedName("cluster_id")
        private final String clusterId;
    }

    public static ServerImpl create(@NonNull Struct struct) throws IOException {
        var serverStruct = ProtoStruct.deserialize(struct, ServerStruct.class);
        var server = new ServerImpl(serverStruct.id);
        server.update(serverStruct);
        return server;
    }

    public static void update(@NonNull ServerImpl server, @NonNull Struct struct) throws IOException {
        var serverStruct = ProtoStruct.deserialize(struct, ServerStruct.class);
        server.update(serverStruct);
    }

    private void update(ServerStruct serverStruct) {
        this.clusterId = serverStruct.clusterId;
        this.labels = Labels.fromMap(serverStruct.labels);
    }

    @SneakyThrows
    public Struct toStruct() {
        var server = new ServerStruct(id, labels, clusterId);
        return server.serialize();
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Labels getLabels() {
        return this.labels;
    }

    @Override
    public Optional<ProxyCluster> getCluster() {
        return Cheetah.get().getClusters().getById(clusterId);
    }

    @Override
    public ProxyPlayerCollection getPlayers() {
        return new PlayerCollectionImpl();
    }
}
