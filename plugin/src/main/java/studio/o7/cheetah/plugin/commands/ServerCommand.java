package studio.o7.cheetah.plugin.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.cheetah.plugin.commands.args.ServerArgument;
import studio.o7.cheetah.plugin.utils.CommandFeedback;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

@UtilityClass
public class ServerCommand {

    public LiteralArgumentBuilder<CommandSourceStack> createCommand() {
        return Commands.literal("server")
                .requires(player("cheetah.server"))
                .then(Commands.argument("server", new ServerArgument())
                        .requires(player("cheetah.server.jump"))
                        .then(Commands.argument("action", new ActionArgument())
                                .executes(ctx -> {
                                    var action = ctx.getArgument("action", Action.class);

                                    if (action == Action.JUMP)
                                        return runJump(ctx);

                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                        .executes(ServerCommand::runJump)
                );
    }

    private int runJump(CommandContext<CommandSourceStack> ctx) {
        var server = ctx.getArgument("server", ProxyServer.class);
        var sender = ctx.getSource().getSender();

        sender.sendMessage("Jump to server " + server.getId());

        return Command.SINGLE_SUCCESS;
    }

    private Predicate<CommandSourceStack> player(@NonNull String perm) {
        return sender -> {
            if (!(sender.getSender() instanceof Player player)) return false;
            return player.hasPermission(perm);
        };
    }

    @NullMarked
    @RequiredArgsConstructor
    enum Action {
        INFO("cheetah.server.info"),
        JUMP("cheetah.server.jump"),
        STOP("cheetah.server.stop"),
        SEND("cheetah.server.send");

        final String perm;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @NullMarked
    static class ActionArgument implements CustomArgumentType.Converted<Action, String> {
        static final DynamicCommandExceptionType ERROR_INVALID_ACTION = new DynamicCommandExceptionType(ActionArgument::invalid);

        private static Message invalid(Object action) {
            return CommandFeedback.error("'" + action + "' is not a valid action!");
        }

        @Override
        public Action convert(String nativeType) throws CommandSyntaxException {
            try {
                return Action.valueOf(nativeType.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ignored) {
                throw ERROR_INVALID_ACTION.create(nativeType);
            }
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            for (var action : Action.values()) {
                if (context.getSource() instanceof CommandSourceStack stack && !stack.getSender().hasPermission(action.perm)) continue;

                String name = action.toString();

                if (name.startsWith(builder.getRemainingLowerCase())) {
                    builder.suggest(name);
                }
            }
            return builder.buildFuture();
        }

        @Override
        public ArgumentType<String> getNativeType() {
            return StringArgumentType.word();
        }
    }
}
