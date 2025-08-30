package studio.o7.cheetah.plugin.api.player;

import lombok.NonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a collection of {@link ProxyPlayer} instances
 * currently online on the proxy system.
 * <p>
 * Provides lookup methods by player identifiers such as username,
 * unique UUID (for Java or converted Bedrock IDs),
 * and Xbox Unique ID (XUID for Bedrock Edition players).
 * </p>
 */
public interface ProxyPlayerCollection extends Collection<ProxyPlayer> {

    /**
     * Returns a player by their current username.
     * <p>
     * Note that usernames may change over time and are not guaranteed
     * to be a stable identifier. For persistent references,
     * prefer {@link #getByUniqueId(UUID)} or {@link #getByXboxUniqueId(long)}.
     * </p>
     *
     * @param username the current username (case-insensitive depending on implementation)
     * @return an optional player with the given name
     */
    Optional<ProxyPlayer> getByName(@NonNull String username);

    /**
     * Returns a player by their unique identifier.
     * <p>
     * For Java Edition players, this is the Mojang-assigned UUID.
     * For Bedrock Edition players, this is a converted UUID based on the Xbox Unique ID (XUID).
     * </p>
     *
     * @param uuid the player's unique identifier
     * @return an optional player with the given UUID
     */
    Optional<ProxyPlayer> getByUniqueId(@NonNull UUID uuid);

    /**
     * Returns a Bedrock Edition player by their Xbox Unique ID (XUID).
     * <p>
     * For Java Edition players, no XUID exists and this lookup will always return empty.
     * </p>
     *
     * @param xuid the Xbox Unique ID of a Bedrock Edition player
     * @return an optional player with the given XUID
     */
    Optional<ProxyPlayer> getByXboxUniqueId(long xuid);
}
