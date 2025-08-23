package studio.o7.cheetah.plugin.cluster;

import com.google.protobuf.Struct;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.CheetahPlugin;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.cluster.ProxyClusterCollection;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.listener.Listener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public final class ClusterCollectionImpl implements ProxyClusterCollection {
    private static final Set<ClusterHolder> HOLDERS = new ObjectArraySet<>();

    public ClusterCollectionImpl() {
        var plugin = JavaPlugin.getPlugin(CheetahPlugin.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task ->
                new ObjectArraySet<>(HOLDERS).forEach(ClusterHolder::gc), 20, 100);
        var listener = new Listener("cheetah.cluster.*", 0) {
            @SneakyThrows
            @Override
            public void onCall(studio.o7.octopus.sdk.gen.api.v1.@NonNull Object obj) {
                var cluster = ClusterImpl.create(obj.getData());
                var holder = getByID(cluster.getId());
                if (holder == null) return;
                var clusterRef = holder.clusterRef.get();
                if (clusterRef == null) {
                    holder.gc();
                    return;
                }
                ClusterImpl.update(clusterRef, obj.getData());
            }
        };
        Octopus.get().registerListener(listener);
    }

    @Getter
    private static final class ClusterHolder {
        private final WeakReference<ClusterImpl> clusterRef;

        private ClusterHolder(@NonNull Struct struct) throws IOException {
            var cluster = ClusterImpl.create(struct);
            this.clusterRef = new WeakReference<>(cluster);
        }

        private void gc() {
            if (clusterRef.get() != null) return;
            HOLDERS.remove(this);
        }

        private Optional<ProxyCluster> get() {
            return Optional.ofNullable(clusterRef.get());
        }
    }

    private static ClusterHolder query(@NonNull String id) {
        return Octopus.get().get("cheetah.cluster." + id)
                .stream()
                .map(entry -> {
                    try {
                        return new ClusterHolder(entry.getObject().getData());
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }).findAny().orElse(null);
    }

    private static ClusterHolder getByID(@NonNull String id) {
        return HOLDERS.stream()
                .filter(holder -> {
                    if (holder.get().isEmpty()) return false;
                    return holder.get().get().getId().equals(id);
                }).findAny().orElse(null);
    }

    private static Collection<ProxyCluster> liveClusters() {
        return HOLDERS.stream()
                .map(ClusterHolder::get)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public Optional<ProxyCluster> getById(@NonNull String id) {
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
        return liveClusters().contains(o);
    }

    @Override
    public @NotNull Iterator<ProxyCluster> iterator() {
        return liveClusters().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return liveClusters().toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return liveClusters().toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return liveClusters().containsAll(c);
    }

    @Override
    public boolean add(ProxyCluster proxyCluster) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends ProxyCluster> c) {
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
