package studio.o7.cheetah.plugin.cluster;

import com.google.protobuf.Struct;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.Blockage;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.cheetah.plugin.api.utils.Labels;
import studio.o7.octopus.sdk.structs.ProtoStruct;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public final class ClusterImpl implements ProxyCluster {
    private final String id;
    private Labels labels;

    @AllArgsConstructor
    private static final class ClusterStruct extends ProtoStruct {
        private final String id;
        private final Map<String, String> labels;
    }

    public static ClusterImpl create(@NonNull Struct struct) throws IOException {
        var clusterStruct = ProtoStruct.deserialize(struct, ClusterStruct.class);
        var cluster = new ClusterImpl(clusterStruct.id);
        cluster.update(clusterStruct);
        return cluster;
    }

    public static void update(@NonNull ClusterImpl cluster, @NonNull Struct struct) throws IOException {
        var clusterStruct = ProtoStruct.deserialize(struct, ClusterStruct.class);
        cluster.update(clusterStruct);
    }

    private void update(ClusterStruct clusterStruct) {
        this.labels = Labels.fromMap(clusterStruct.labels);
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
    public ProxyServerCollection getServers() {
        return null;
    }

    @Override
    public ProxyPlayerCollection getPlayers() {
        return null;
    }

    @Override
    public Optional<Blockage> getBlock(@NonNull String id) {
        return Optional.empty();
    }
}
