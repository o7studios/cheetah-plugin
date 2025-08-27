package studio.o7.cheetah.plugin.api.events;

import com.google.gson.annotations.SerializedName;
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

    @SerializedName("login_status")
    private final int loginStatus;

    public ProxyDisconnectEvent(@NonNull ProxyPlayer player, int loginStatus) {
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

    public LoginStatus getLoginStatus() {
        return LoginStatus.statusById(loginStatus).orElse(LoginStatus.SUCCESSFUL);
    }

    @RequiredArgsConstructor
    @Getter
    public enum LoginStatus {
        SUCCESSFUL(0),
        CANCELED_BY_USER(1),
        CANCELED_BY_PROXY(2),
        CANCELED_BY_PLAYER_BEFORE_COMPLETE(3);

        private final int id;

        public static Optional<LoginStatus> statusById(int id) {
            return Arrays.stream(values()).filter(status -> status.id == id).findAny();
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
