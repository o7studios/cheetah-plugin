package studio.o7.cheetah.plugin.events;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.resource.ResourcePackInfo;
import org.bukkit.Bukkit;
import studio.o7.cheetah.plugin.api.events.ProxyResourcePackStatusEvent;

import java.net.URI;
import java.util.UUID;

import static studio.o7.cheetah.plugin.api.events.ProxyResourcePackStatusEvent.PackStatus.DISCARDED_RESPONSE;
import static studio.o7.cheetah.plugin.api.events.ProxyResourcePackStatusEvent.PackStatus.getStatusByName;

@AllArgsConstructor
@Getter
public final class ProxyResourcePackStatusEventImpl extends ProxyEventImpl {
    private final UUID uuid;
    @SerializedName("pack_status")
    private final String packStatus;
    @SerializedName("pack_id")
    private final UUID packId;
    private final String url;
    private final String hash;
    @SerializedName("override_kick")
    private boolean overrideKick;

    @Override
    public void call() {
        var status = getStatusByName(packStatus).orElse(DISCARDED_RESPONSE);
        var info = ResourcePackInfo.resourcePackInfo(packId, URI.create(url), hash);
        var event = new ProxyResourcePackStatusEvent(getPlayer(uuid), status, info, overrideKick);
        Bukkit.getPluginManager().callEvent(event);
        this.overrideKick = event.isOverrideKick();
    }
}
