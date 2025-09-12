package studio.o7.cheetah.plugin.utils;

import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@UtilityClass
public class EnvUtils {

    public Optional<String> getHostname() {
        return Optional.ofNullable(System.getenv("HOSTNAME"));
    }

    public Optional<String> getNamespace() {
        var env = System.getenv("POD_NAMESPACE");
        if (env != null && !env.isBlank()) return Optional.of(env);

        var nsPath = Path.of("/var/run/secrets/kubernetes.io/serviceaccount/namespace");
        if (Files.exists(nsPath)) {
            try {
                return Optional.of(Files.readString(nsPath));
            } catch (IOException exception) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }
}
