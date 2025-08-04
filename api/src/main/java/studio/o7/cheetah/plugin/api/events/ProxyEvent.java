package studio.o7.cheetah.plugin.api.events;

import org.bukkit.event.Event;

public abstract class ProxyEvent extends Event {
    public ProxyEvent() {
        super(true);
    }
}
