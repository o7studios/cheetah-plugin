package studio.o7.cheetah.plugin.server;

import com.google.protobuf.Struct;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.CheetahPlugin;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.cheetah.plugin.api.server.ProxyServerCollection;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.listener.Listener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public final class ServerCollectionImpl implements ProxyServerCollection {
    private static final Set<ServerHolder> HOLDERS = new ObjectArraySet<>();

    public ServerCollectionImpl() {
        var plugin = JavaPlugin.getPlugin(CheetahPlugin.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task ->
                new ObjectArraySet<>(HOLDERS).forEach(ServerHolder::gc), 20, 100);
        var listener = new Listener("cheetah.server.*", 0) {
            @SneakyThrows
            @Override
            public void onCall(studio.o7.octopus.sdk.gen.api.v1.@NonNull Object obj) {
                var server = ServerImpl.create(obj.getData());
                var holder = getByID(server.getId());
                if (holder == null) return;
                var serverRef = holder.serverRef.get();
                if (serverRef == null) {
                    holder.gc();
                    return;
                }
                ServerImpl.update(serverRef, obj.getData());
            }
        };
        Octopus.get().registerListener(listener);
    }

    @Getter
    private static final class ServerHolder {
        private final WeakReference<ServerImpl> serverRef;

        private ServerHolder(@NonNull Struct struct) throws IOException {
            var server = ServerImpl.create(struct);
            this.serverRef = new WeakReference<>(server);

        }

        private void gc() {
            if (serverRef.get() != null) return;
            HOLDERS.remove(this);
        }

        private Optional<ProxyServer> get() {
            return Optional.ofNullable(serverRef.get());
        }
    }

    private static ServerHolder query(@NonNull String something) {
        return Octopus.get().get("cheetah.server." + something)
                .stream()
                .map(entry -> {
                    try {
                        return new ServerHolder(entry.getObject().getData());
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }).findAny().orElse(null);
    }

    private static ServerHolder getByID(@NonNull String id) {
        return HOLDERS.stream()
                .filter(holder -> {
                    if (holder.get().isEmpty()) return false;
                    return holder.get().get().getId().equals(id);
                }).findAny().orElse(null);
    }

    private static Collection<ProxyServer> liveServers() {
        return HOLDERS.stream()
                .map(ServerHolder::get)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public Optional<ProxyServer> getById(@NonNull String id) {
        var holder = getByID(id);
        if (holder == null) {
            var newHolder = query(id);
            HOLDERS.add(newHolder);
            return newHolder.get();
        }
        return holder.get();
    }

    @Override
    public int size() {
        return HOLDERS.size();
    }

    @Override
    public boolean isEmpty() {
        return HOLDERS.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return liveServers().contains(o);
    }

    @Override
    public @NotNull Iterator<ProxyServer> iterator() {
        return liveServers().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return liveServers().toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return liveServers().toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return liveServers().containsAll(c);
    }

    @Override
    public boolean add(ProxyServer proxyCluster) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends ProxyServer> c) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Immutable collection");
    }
}
