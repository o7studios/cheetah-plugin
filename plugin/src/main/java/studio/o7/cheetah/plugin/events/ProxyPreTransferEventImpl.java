package studio.o7.cheetah.plugin.events;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import studio.o7.cheetah.plugin.api.events.ProxyPreTransferEvent;
import java.util.UUID;

@AllArgsConstructor
@Getter
public final class ProxyPreTransferEventImpl extends ProxyEventImpl {
    private final UUID uuid;
    @SerializedName("original_host")
    private final String originalHost;
    @SerializedName("target_host")
    private String targetHost;
    private boolean denied;

    @Override
    public void call() {
        var originalAddress = getAddress(originalHost);
        var targetAddress = getAddress(targetHost);
        var event = new ProxyPreTransferEvent(getPlayer(uuid), originalAddress, targetAddress, denied);
        Bukkit.getPluginManager().callEvent(event);
        this.denied = event.isDenied();
        this.targetHost = asString(event.getTargetAddr());
    }
}
