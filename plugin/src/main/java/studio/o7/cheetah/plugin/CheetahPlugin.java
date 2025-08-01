package studio.o7.cheetah.plugin;

import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import studio.o7.cheetah.plugin.api.Cheetah;

@SuppressWarnings("UnstableApiUsage")
@Getter
public final class CheetahPlugin extends JavaPlugin implements PluginInstance {
    private CheetahImpl cheetah;

    public CheetahPlugin() {
        Unsafe.setInstance(this);
    }

    @Override
    public void onEnable() {
        cheetah = new CheetahImpl();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public Cheetah get() {
        return this.cheetah;
    }
}
