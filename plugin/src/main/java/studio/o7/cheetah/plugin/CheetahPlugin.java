package studio.o7.cheetah.plugin;

import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.util.Config;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import studio.o7.cheetah.plugin.api.Cheetah;
import studio.o7.cheetah.plugin.commands.ServerCommand;
import studio.o7.cheetah.plugin.utils.EnvUtils;

@Getter
@Slf4j
public final class CheetahPlugin extends JavaPlugin implements PluginInstance {
    public static final String POD_NAME = EnvUtils.getHostname().orElse(null);
    public static final String POD_NAMESPACE = EnvUtils.getNamespace().orElse(null);
    public static String POD_NAMESPACE_ID;

    private CheetahImpl cheetah;

    public CheetahPlugin() {
        Unsafe.setInstance(this);
    }

    @Override
    public void onLoad() {
        if (POD_NAME == null || POD_NAME.isBlank())
            throw new IllegalStateException("Pod name is not queryable. Is this server really running in Kubernetes?");
        if (POD_NAMESPACE == null || POD_NAMESPACE.isBlank())
            throw new IllegalStateException("Pod namespace is not queryable. Is this really server running in Kubernetes?");

        log.info("Running in Pod: `{}`, Namespace: `{}`", POD_NAME, POD_NAMESPACE);

        try {
            var client = Config.fromCluster();
            Configuration.setDefaultApiClient(client);

            var api = new CoreV1Api();

            var nsObj = api.readNamespace(POD_NAMESPACE, null);

            if (nsObj == null)
                throw new IllegalStateException("Cannot read this pods namespace via k8s client");

            var meta = nsObj.getMetadata();

            if (meta == null)
                throw new IllegalStateException("Cannot read this pods namespace meta via k8s client");

            POD_NAMESPACE_ID = nsObj.getMetadata().getUid();

            log.info("Got Namespace UID: {}", POD_NAMESPACE_ID);
        } catch (Exception exception) {
            log.error("Cannot initialize K8s metadata", exception);
            Bukkit.shutdown();
            throw new IllegalStateException(exception);
        }
    }

    @Override
    public void onEnable() {
        cheetah = new CheetahImpl();

        var pm = Bukkit.getPluginManager();

        pm.registerEvents(CheetahImpl.ONLINE_PLAYERS, this);

        var lifecycleManager = getLifecycleManager();

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
