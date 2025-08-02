package studio.o7.cheetah.plugin;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.commands.ServerCommand;

@Getter
public final class CheetahPlugin extends JavaPlugin implements PluginInstance {
    private CheetahImpl cheetah;

    public CheetahPlugin() {
        Unsafe.setInstance(this);
    }

    @Override
    public void onEnable() {
        cheetah = new CheetahImpl();
        LifecycleEventManager<@NotNull Plugin> lifecycleManager = getLifecycleManager();

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register(ServerCommand.createCommand().build());
        });
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
