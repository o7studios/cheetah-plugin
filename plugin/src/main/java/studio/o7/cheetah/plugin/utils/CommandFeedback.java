package studio.o7.cheetah.plugin.utils;

import com.mojang.brigadier.Message;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

@UtilityClass
public class CommandFeedback {
    private final TextColor infoColor = TextColor.fromHexString("#999999");
    private final TextColor errorColor = TextColor.fromHexString("#F83A22");

    public Message error(String msg) {
        return MessageComponentSerializer.message().serialize(Component.text(msg, errorColor));
    }

    public Message info(String msg) {
        return MessageComponentSerializer.message().serialize(Component.text(msg, infoColor));
    }
}
