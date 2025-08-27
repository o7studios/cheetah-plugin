package studio.o7.cheetah.plugin.api.events;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @SerializedName("pack_status")
    private final int status;

    @NonNull
    @SerializedName("pack_info")
    private final ResourcePackInfo info;

    @SerializedName("override_kick")
    private boolean overrideKick = false;

    public ProxyResourcePackStatusEvent(@NonNull ProxyPlayer player, int status, @NonNull ResourcePackInfo info, boolean overrideKick) {
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

    public PackStatus getStatus() {
        return PackStatus.statusById(this.status).orElse(PackStatus.DECLINED_RESPONSE);
    }

    @RequiredArgsConstructor
    @Getter
    public enum PackStatus {
        DECLINED_RESPONSE(0),
        SUCCESSFUL_RESPONSE(1),
        FAILED_DOWNLOAD_RESPONSE(2),
        ACCEPTED_RESPONSE(3),
        DOWNLOADED_RESPONSE(4),
        INVALID_URL_RESPONSE(5),
        FAILED_TO_RELOAD_RESPONSE(6),
        DISCARDED_RESPONSE(7);

        private final int id;

        public static Optional<PackStatus> statusById(int id) {
            return Arrays.stream(values()).filter(status -> status.id == id).findAny();
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }
}
