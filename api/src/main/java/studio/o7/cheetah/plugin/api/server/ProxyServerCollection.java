package studio.o7.cheetah.plugin.api.server;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;

public interface ProxyServerCollection extends Collection<ProxyServer> {

    Optional<ProxyServer> getById(@NonNull String id);
}
