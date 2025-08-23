package studio.o7.cheetah.plugin.player;

import com.google.protobuf.Struct;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.CheetahPlugin;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.octopus.plugin.api.Octopus;
import studio.o7.octopus.plugin.api.listener.Listener;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class PlayerCollectionImpl implements ProxyPlayerCollection, org.bukkit.event.Listener {
    private static final Set<PlayerHolder> HOLDERS = new ObjectArraySet<>();

    public PlayerCollectionImpl() {
        var plugin = JavaPlugin.getPlugin(CheetahPlugin.class);
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task ->
                new ObjectArraySet<>(HOLDERS).forEach(PlayerHolder::gc), 20, 100);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerLoginEvent(PlayerJoinEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        Octopus.get().get("cheetah.player." + uuid)
                .stream()
                .map(entry -> {
                    try {
                        return new PlayerHolder(entry.getObject().getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).findAny().ifPresent(HOLDERS::add);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        var uuid = event.getPlayer().getUniqueId();
        var holder = getByUUID(uuid);
        holder.gc();
    }

    @Getter
    private static final class PlayerHolder {
        private final WeakReference<PlayerImpl> playerRef;
        private final Listener listener;

        private PlayerHolder(@NonNull Struct struct) throws IOException {
            var player = PlayerImpl.create(struct);
            this.playerRef = new WeakReference<>(player);
            this.listener = new Listener("cheetah.player." + player.getUuid(), 0) {
                @SneakyThrows
                @Override
                public void onCall(studio.o7.octopus.sdk.gen.api.v1.@NonNull Object object) {
                    var player = playerRef.get();
                    if (player == null) {
                        gc();
                        return;
                    }
                    PlayerImpl.update(player, object.getData());
                }
            };
            Octopus.get().registerListener(listener);
        }

        private void gc() {
            if (playerRef.get() != null) return;
            HOLDERS.remove(this);
            Octopus.get().unregisterListener(listener);
        }

        private Optional<ProxyPlayer> get() {
            return Optional.ofNullable(playerRef.get());
        }
    }

    private static PlayerHolder query(@NonNull String something) {
        return Octopus.get().get("cheetah.player." + something)
                .stream()
                .map(entry -> {
                    try {
                        return new PlayerHolder(entry.getObject().getData());
                    } catch (Exception exception) {
                        throw new RuntimeException(exception);
                    }
                }).findAny().orElse(null);
    }

    private static PlayerHolder getByUUID(@NonNull UUID uuid) {
        return HOLDERS.stream()
                .filter(holder -> {
                    if (holder.get().isEmpty()) return false;
                    return holder.get().get().getUniqueId().equals(uuid);
                }).findAny().orElse(null);
    }

    private static PlayerHolder getByUsername(@NonNull String name) {
        return HOLDERS.stream()
                .filter(holder -> {
                    if (holder.get().isEmpty()) return false;
                    return holder.get().get().getUsername().equalsIgnoreCase(name);
                }).findAny().orElse(null);
    }

    private static PlayerHolder getByXuid(@NonNull String name) {
        return HOLDERS.stream()
                .filter(holder -> {
                    if (holder.get().isEmpty()) return false;
                    var xuid = holder.get().get().getXboxUniqueId().orElse(null);
                    if (xuid == null) return false;
                    return xuid.equalsIgnoreCase(name);
                }).findAny().orElse(null);
    }

    private static Collection<ProxyPlayer> livePlayers() {
        return HOLDERS.stream()
                .map(PlayerHolder::get)
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    public Optional<ProxyPlayer> getByName(@NonNull String username) {
        var holder = getByUsername(username);
        if (holder == null) {
            var newHolder = query(username);
            HOLDERS.add(newHolder);
            return newHolder.get();
        }
        return holder.get();
    }

    @Override
    public Optional<ProxyPlayer> getByUniqueId(@NonNull UUID uuid) {
        var holder = getByUUID(uuid);
        if (holder == null) {
            var newHolder = query(uuid.toString());
            HOLDERS.add(newHolder);
            return newHolder.get();
        }
        return holder.get();
    }

    @Override
    public Optional<ProxyPlayer> getByXboxUniqueId(@NonNull String xuid) {
        var holder = getByXuid(xuid);
        if (holder == null) {
            var newHolder = query(xuid);
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
        return HOLDERS.stream().anyMatch(holder -> Objects.equals(o, holder.get().orElse(null)));
    }

    @Override
    public @NotNull Iterator<ProxyPlayer> iterator() {
        return livePlayers().iterator();
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        return livePlayers().toArray();
    }

    @Override
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        return livePlayers().toArray(a);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return livePlayers().containsAll(c);
    }

    @Override
    public boolean add(ProxyPlayer proxyPlayer) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Immutable collection");
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends ProxyPlayer> c) {
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
