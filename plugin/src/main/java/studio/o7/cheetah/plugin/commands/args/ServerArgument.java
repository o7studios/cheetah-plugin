package studio.o7.cheetah.plugin.commands.args;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.jspecify.annotations.NullMarked;
import studio.o7.cheetah.plugin.api.cluster.ProxyCluster;
import studio.o7.cheetah.plugin.api.player.ProxyPlayerCollection;
import studio.o7.cheetah.plugin.api.server.ProxyServer;
import studio.o7.cheetah.plugin.api.utils.Labels;
import studio.o7.cheetah.plugin.utils.CommandFeedback;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class ServerArgument implements CustomArgumentType.Converted<ProxyServer, String> {
    static final DynamicCommandExceptionType ERROR_SERVER_NOT_FOUND = new DynamicCommandExceptionType(ServerArgument::notFound);

    private static Message notFound(Object action) {
        return CommandFeedback.error("Server '" + action + "' not found!");
    }

    @Override
    public ProxyServer convert(String nativeType) throws CommandSyntaxException {
        try {
            return new ProxyServer() {
                @Override
                public String getId() {
                    return "";
                }

                @Override
                public Labels getLabels() {
                    return null;
                }

                @Override
                public Optional<ProxyCluster> getCluster() {
                    return Optional.empty();
                }

                @Override
                public ProxyPlayerCollection getPlayers() {
                    return null;
                }
            };
        } catch (IllegalArgumentException ignored) {
            throw ERROR_SERVER_NOT_FOUND.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if ("test".startsWith(builder.getRemainingLowerCase())) {
            builder.suggest("test");
        }

        /*for (var server : Cheetah.get().getServers()) {
            server.getId();
        }
         */
        return builder.buildFuture();
    }

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }
}
