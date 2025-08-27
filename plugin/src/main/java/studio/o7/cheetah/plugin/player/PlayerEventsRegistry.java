package studio.o7.cheetah.plugin.player;

import com.google.gson.GsonBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.NonNull;
import org.bukkit.Bukkit;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.events.ProxyDisconnectEvent;
import studio.o7.cheetah.plugin.api.events.ProxyPreTransferEvent;
import studio.o7.cheetah.plugin.api.events.ProxyResourcePackStatusEvent;
import studio.o7.cheetah.plugin.api.events.login.ProxyLoginEvent;
import studio.o7.cheetah.plugin.api.events.login.ProxyPostLoginEvent;
import studio.o7.cheetah.plugin.api.events.server.*;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.listener.Listener;
import studio.o7.octopus.plugin.api.parser.StructParser;
import studio.o7.octopus.sdk.gen.api.v1.Object;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public final class PlayerEventsRegistry {
    private static final GsonBuilder BUILDER = new GsonBuilder()
            .registerTypeAdapter(ProxyPlayer.class, new ProxyPlayer.Adapter())
            .registerTypeAdapter(ProxyServer.class, new ProxyServer.Adapter())
            .registerTypeAdapter(ProxyCluster.class, new ProxyCluster.Adapter());

    private static final StructParser<ProxyChooseInitialServerEvent> CHOOSE_INITIAL_SERVER =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyDisconnectEvent> DISCONNECT =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyLoginEvent> LOGIN =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyPostLoginEvent> POST_LOGIN =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyPreTransferEvent> PRE_TRANSFER =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyResourcePackStatusEvent> PACK_STATUS =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyKickedFromServerEvent> KICKED =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyServerPreConnectEvent> SERVER_PRE_CONNECT =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyServerConnectedEvent> SERVER_CONNECTED =
            new StructParser<>(BUILDER);
    private static final StructParser<ProxyServerPostConnectEvent> SERVER_POST_CONNECTED =
            new StructParser<>(BUILDER);

    private final Object2ObjectMap<UUID, Listener> listeners = new Object2ObjectArrayMap<>();

    public synchronized void registerPlayer(@NonNull ProxyPlayer player) {
        var listener = new Listener("cheetah.event." + player.getUniqueId() + ".*", 10) {

            @Override
            public void onCall(@NonNull Object obj) {
                var key = obj.getKey();
                var data = obj.getData();

                try {
                    if (key.endsWith("server.choose-initial")) {
                        var event = CHOOSE_INITIAL_SERVER.toObject(data, ProxyChooseInitialServerEvent.class);
                        Bukkit.getPluginManager().callEvent(event);
                        return;
                    }
                    if (key.endsWith("disconnect")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyDisconnectEvent event;
                            try {
                                event = DISCONNECT.toObject(data, ProxyDisconnectEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                        return;
                    }
                    if (key.endsWith("login")) {
                        var event = LOGIN.toObject(data, ProxyLoginEvent.class);
                        Bukkit.getPluginManager().callEvent(event);
                        return;
                    }
                    if (key.endsWith("post-login")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyPostLoginEvent event;
                            try {
                                event = POST_LOGIN.toObject(data, ProxyPostLoginEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                        return;
                    }
                    if (key.endsWith("pre-transfer")) {
                        var event = PRE_TRANSFER.toObject(data, ProxyPreTransferEvent.class);
                        Bukkit.getPluginManager().callEvent(event);
                        return;
                    }
                    if (key.endsWith("pack-status")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyResourcePackStatusEvent event;
                            try {
                                event = PACK_STATUS.toObject(data, ProxyResourcePackStatusEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                        return;
                    }
                    if (key.endsWith("server.kicked")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyKickedFromServerEvent event;
                            try {
                                event = KICKED.toObject(data, ProxyKickedFromServerEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                        return;
                    }
                    if (key.endsWith("server.pre-connect")) {
                        var event = SERVER_PRE_CONNECT.toObject(data, ProxyServerPreConnectEvent.class);
                        Bukkit.getPluginManager().callEvent(event);
                        return;
                    }
                    if (key.endsWith("server.connected")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyServerConnectedEvent event;
                            try {
                                event = SERVER_CONNECTED.toObject(data, ProxyServerConnectedEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                        return;
                    }
                    if (key.endsWith("server.post-connected")) {
                        CompletableFuture.runAsync(() -> {
                            ProxyServerPostConnectEvent event;
                            try {
                                event = SERVER_POST_CONNECTED.toObject(data, ProxyServerPostConnectEvent.class);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            Bukkit.getPluginManager().callEvent(event);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        listeners.put(player.getUniqueId(), listener);
        Octopus.get().registerListener(listener);
    }

    public synchronized void unregisterPlayer(@NonNull UUID uuid) {
        var listener = listeners.get(uuid);
        if (listener == null)  return;
        this.listeners.remove(uuid);
        Octopus.get().unregisterListener(listener);
    }
}