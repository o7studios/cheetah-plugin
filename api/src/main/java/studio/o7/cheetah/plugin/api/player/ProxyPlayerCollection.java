package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ProxyPlayerCollection extends Collection<ProxyPlayer> {

    default Optional<ProxyPlayer> getByName(@NonNull String username) {
        return stream()
                .filter(player -> player.getUsername().equalsIgnoreCase(username))
                .findAny();
    }

    default Optional<ProxyPlayer> getByUuid(@NonNull UUID uuid) {
        return stream()
                .filter(player -> player.getUniqueId().equals(uuid))
                .findAny();
    }

    default Optional<ProxyPlayer> getByXboxUniqueId(@NonNull String xuid) {
        return stream()
                .filter(player -> player.getXboxUniqueId().orElse("").equalsIgnoreCase(xuid))
                .findAny();
    }
}
