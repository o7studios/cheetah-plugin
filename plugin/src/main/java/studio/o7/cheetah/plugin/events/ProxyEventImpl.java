package studio.o7.cheetah.plugin.events;

import lombok.NonNull;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.octopus.sdk.structs.ProtoStruct;

import java.net.InetSocketAddress;
import java.util.UUID;

public abstract class ProxyEventImpl extends ProtoStruct {

    protected final ProxyPlayer getPlayer(@NonNull UUID uuid) {
        var player = Cheetah.get().getPlayers().getByUniqueId(uuid).orElse(null);
        if (player == null) throw new IllegalStateException("Cannot find player with uuid " + uuid);
        return player;
    }

    protected final ProxyServer getServer(@NonNull String id) {
        var server = Cheetah.get().getServers().getById(id).orElse(null);
        if (server == null) throw new IllegalStateException("Cannot find server with id " + id);
        return server;
    }

    protected final InetSocketAddress getAddress(@NonNull String host) {
        var parts = host.split(":", 2);
        var hostname = parts[0];
        var port = Integer.parseInt(parts[1]);
        return InetSocketAddress.createUnresolved(hostname, port);
    }

    protected final String asString(@NonNull InetSocketAddress address) {
        return address.getHostString() + ":"  + address.getPort();
    }

    public abstract void call() throws Exception;
}
