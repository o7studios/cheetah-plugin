package studio.o7.cheetah.plugin.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent;

import java.util.UUID;

import static studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent.LoginStatus.SUCCESSFUL;
import static studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent.LoginStatus.getStatusByName;

@AllArgsConstructor
@Getter
public final class ProxyDisconnectEventImpl extends ProxyEventImpl {
    private final UUID uuid;
    private final String status;

    @Override
    public void call() {
        var status = getStatusByName(this.status).orElse(SUCCESSFUL);
        var event = new ProxyDisconnectEvent(getPlayer(uuid), status);
        Bukkit.getPluginManager().callEvent(event);
    }
}
