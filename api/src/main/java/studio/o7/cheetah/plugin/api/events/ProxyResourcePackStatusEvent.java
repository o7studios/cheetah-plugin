package studio.o7.cheetah.plugin.api.events;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.resource.ResourcePackInfo;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.player.ProxyPlayer;

import java.util.Arrays;
import java.util.Optional;

/**
 * {@link ProxyResourcePackStatusEvent} is fired when the status of a resource pack sent to the player by the server is
 * changed. Depending on the result of this event (which the proxy will wait until completely fired),
 * the player may be kicked from the network.
 */
@Getter
public final class ProxyResourcePackStatusEvent extends ProxyPlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    @NonNull
    private final PackStatus status;
    @NonNull
    private final ResourcePackInfo info;

    private boolean overrideKick = false;

    public ProxyResourcePackStatusEvent(@NonNull ProxyPlayer player, @NonNull PackStatus status, @NonNull ResourcePackInfo info, boolean overrideKick) {
        super(player);
        this.status = status;
        this.info = info;
        this.overrideKick = overrideKick;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    /**
     *  Method can set to true to prevent ResourcePackInfo.ShouldForce()
     *  from kicking the player. Overwriting this kick is only possible on versions older than 1.17,
     *  as the client or server will enforce this regardless. Cancelling the resulting
     *  kick-events will not prevent the player from disconnecting from the proxy.
     */
    public void setOverrideKick(boolean overrideKick) {
        this.overrideKick = overrideKick;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PackStatus {
        SUCCESSFUL_RESPONSE("successful"),
        DECLINED_RESPONSE("declined"),
        FAILED_DOWNLOAD_RESPONSE("failed_download"),
        ACCEPTED_RESPONSE("accepted"),
        DOWNLOADED_RESPONSE("downloaded"),
        INVALID_URL_RESPONSE("invalid_url"),
        FAILED_TO_RELOAD_RESPONSE("failed_reload"),
        DISCARDED_RESPONSE("discarded");

        private final String name;

        public static Optional<PackStatus> getStatusByName(@NonNull String name) {
            return Arrays.stream(values()).filter(status -> status.name.equalsIgnoreCase(name)).findAny();
        }
    }
}
