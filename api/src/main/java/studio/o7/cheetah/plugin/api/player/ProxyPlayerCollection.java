package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ProxyPlayerCollection extends Collection<ProxyPlayer> {

    Optional<ProxyPlayer> getByName(@NonNull String username);

    Optional<ProxyPlayer> getByUniqueId(@NonNull UUID uuid);

    Optional<ProxyPlayer> getByXboxUniqueId(@NonNull String xuid);
}
