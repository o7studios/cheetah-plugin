package studio.o7.cheetah.plugin.player;

import com.google.gson.annotations.SerializedName;
import com.google.protobuf.Struct;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.Blockage;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.player.bedrock.Device;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.octopus.sdk.structs.ProtoStruct;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public final class PlayerImpl implements ProxyPlayer {
    private final UUID uuid;
    private final Collection<Blockage> blockages = new ObjectArraySet<>();
    private String username;
    private String xuid;
    private Device device;
    private String currentServerId;
    private String currentClusterId;

    @AllArgsConstructor
    public static final class PlayerStruct extends ProtoStruct {
        private final UUID uuid;
        private final String username;
        private final String xuid;
        private final String device;
        @SerializedName("current_server_id")
        private final String currentServerId;
        @SerializedName("current_cluster_id")
        private final String currentClusterId;
    }

    public static PlayerImpl create(@NonNull Struct struct) throws IOException {
        var playerStruct = ProtoStruct.deserialize(struct, PlayerStruct.class);
        var player = new PlayerImpl(playerStruct.uuid);
        player.update(playerStruct);
        return player;
    }

    public static void update(@NonNull PlayerImpl player, @NonNull Struct struct) throws IOException {
        var playerStruct = ProtoStruct.deserialize(struct, PlayerStruct.class);
        player.update(playerStruct);
    }

    private void update(PlayerStruct playerStruct) {
        this.username = playerStruct.username;
        this.xuid = playerStruct.xuid;
        this.device = Device.getDeviceByName(playerStruct.device).orElse(null);
        this.currentServerId = playerStruct.currentServerId;
        this.currentClusterId = playerStruct.currentClusterId;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public Optional<String> getXboxUniqueId() {
        return Optional.ofNullable(this.xuid);
    }

    @Override
    public Optional<Device> getDevice() {
        return Optional.ofNullable(this.device);
    }

    @Override
    public boolean isBedrockPlayer() {
        return getXboxUniqueId().isPresent();
    }

    @Override
    public Optional<ProxyServer> getCurrentServer() {
        return Cheetah.get().getServers().getById(this.currentServerId);
    }

    @Override
    public Optional<ProxyCluster> getCurrentCluster() {
        return Cheetah.get().getClusters().getById(this.currentClusterId);
    }

    @Override
    public Collection<Blockage> getBlockages() {
        return this.blockages;
    }
}
