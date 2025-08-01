package studio.o7.cheetah.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

import java.util.Arrays;
import java.util.Optional;

@Getter
public final class ProxyDisconnectEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final LoginStatus loginStatus;

    public ProxyDisconnectEvent(@NonNull ProxyPlayer player, LoginStatus loginStatus) {
        super(player);
        this.loginStatus = loginStatus;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LoginStatus {
        SUCCESSFUL("successful"),
        CANCELED_BY_USER("canceled_by_user"),
        CANCELED_BY_PROXY("canceled_by_proxy"),
        CANCELED_BY_PLAYER_BEFORE_COMPLETE("canceled_by_player_before_complete");

        private final String name;

        public static Optional<LoginStatus> getStatusByName(@NonNull String name) {
            return Arrays.stream(values()).filter(status -> status.name.equalsIgnoreCase(name)).findAny();
        }
    }
}
