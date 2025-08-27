package studio.o7.cheetah.plugin.api.events.login;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.events.ProxyPlayerEvent;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

@Getter
@Setter
public final class ProxyLoginEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final boolean denied;
    @NonNull
    @SerializedName("reason_component")
    private Component reason;

    public ProxyLoginEvent(@NonNull ProxyPlayer player, boolean denied, @NonNull Component reason) {
        super(player);
        this.denied = denied;
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}