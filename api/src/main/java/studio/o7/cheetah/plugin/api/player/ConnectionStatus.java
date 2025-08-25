package studio.o7.cheetah.plugin.api.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum ConnectionStatus {
    SUCCESS("success"),
    ALREADY_CONNECTED("already_connected"),
    IN_PROGRESS("in_progress"),
    CANCELLED("cancelled"),
    DISCONNECTED("disconnected");

    private final String name;

    public static Optional<ConnectionStatus> getStatusByName(@NonNull String name) {
        return Arrays.stream(values()).filter(status -> status.name.equalsIgnoreCase(name)).findAny();
    }
}
