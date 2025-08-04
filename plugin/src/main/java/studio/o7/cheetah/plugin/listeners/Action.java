package studio.o7.cheetah.plugin.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent;
import studio.o7.cheetah.plugin.api.events.block.ProxyBlockEvent;
import studio.o7.cheetah.plugin.api.events.login.ProxyLoginEvent;
import studio.o7.cheetah.plugin.api.events.login.ProxyPostLoginEvent;
import studio.o7.cheetah.plugin.api.events.server.ProxyServerConnectedEvent;
import studio.o7.cheetah.plugin.api.events.server.ProxyServerPostConnectEvent;
import studio.o7.cheetah.plugin.api.events.server.ProxyServerPreConnectEvent;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.events.OctopusActionEvent;

import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.MissingFormatArgumentException;
import java.util.UUID;

import static studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent.LoginStatus.SUCCESSFUL;
import static studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent.LoginStatus.getStatusByName;

public final class Action implements Listener {

    @EventHandler
    public void onAction(OctopusActionEvent event) {
        var key = event.getKey();
        var action = event.getAction();
        var data = action.getMetadataMap();

        var cheetah = Cheetah.get();
        var pm = Bukkit.getPluginManager();

        if (key.startsWith("cheetah::login::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                //todo optional response deny
                pm.callEvent(new ProxyLoginEvent(player, false, Component.empty()));
            });
            return;
        }
        if (key.startsWith("cheetah::postlogin::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                pm.callEvent(new ProxyPostLoginEvent(player));
            });
            return;
        }
        if (key.startsWith("cheetah::disconnect::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());
            var status = data.get("status").getStringValue();

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                pm.callEvent(new ProxyDisconnectEvent(player, getStatusByName(status).orElse(SUCCESSFUL)));
            });
            return;
        }
        if (key.startsWith("cheetah::server::connected::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());
            var server = cheetah.getServers().getById(data.get("server").getStringValue()).orElseThrow();
            var previous = cheetah.getServers().getById(data.get("previous").getStringValue()).orElse(null);

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                pm.callEvent(new ProxyServerConnectedEvent(player, server, previous));
            });
            return;
        }
        if (key.startsWith("cheetah::server::preconnected::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());
            var original = cheetah.getServers().getById(data.get("original").getStringValue()).orElseThrow();
            var previous = cheetah.getServers().getById(data.get("previous").getStringValue()).orElse(null);
            var server = cheetah.getServers().getById(data.get("server").getStringValue()).orElse(null);

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
               pm.callEvent(new ProxyServerPreConnectEvent(player, original, previous, server));
            });
            return;
        }
        if (key.startsWith("cheetah::server::postconnected::")) {
            var uuid = UUID.fromString(data.get("uuid").getStringValue());
            var previous = cheetah.getServers().getById(data.get("previous").getStringValue()).orElse(null);

            cheetah.getPlayers().getByUuid(uuid).ifPresent(player -> {
                pm.callEvent(new ProxyServerPostConnectEvent(player, previous));
            });
            return;
        }

    }
}
