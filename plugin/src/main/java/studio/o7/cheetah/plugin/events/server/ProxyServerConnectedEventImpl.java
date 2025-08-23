package studio.o7.cheetah.plugin.events.server;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import studio.o7.cheetah.plugin.api.events.server.ProxyServerConnectedEvent;
import studio.o7.cheetah.plugin.events.ProxyEventImpl;

import java.util.UUID;

@AllArgsConstructor
@Getter
public final class ProxyServerConnectedEventImpl extends ProxyEventImpl {
    private final UUID uuid;
    @SerializedName("server_id")
    private final String serverId;
    @SerializedName("previous_server_id")
    private final String previousServerId;

    @Override
    public void call() throws Exception {
        var server = getServer(serverId);
        var previousServer = getServer(previousServerId);
        var event = new ProxyServerConnectedEvent(getPlayer(uuid), server, previousServer);
        Bukkit.getPluginManager().callEvent(event);
    }
}
