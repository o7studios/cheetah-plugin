package studio.o7.cheetah.plugin;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@UtilityClass
public class Unsafe {
    @Getter
    private PluginInstance instance;

    void setInstance(@NonNull PluginInstance pluginInstance) {
        if (instance != null)
            throw new IllegalArgumentException("Cannot initiate plugin twice");
        instance = pluginInstance;
    }
}
