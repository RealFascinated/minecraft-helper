package cc.fascinated.model.cache;

import cc.fascinated.model.server.MinecraftServer;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * @author Braydon
 */
@AllArgsConstructor @Setter @Getter @ToString
@RedisHash(value = "server", timeToLive = 60L) // 1 minute (in seconds)
public final class CachedMinecraftServer implements Serializable {
    /**
     * The id of this cached server.
     */
    @Id @NonNull private transient final String id;

    /**
     * The cached server.
     */
    @NonNull private final MinecraftServer value;

    /**
     * The unix timestamp of when this
     * server was cached, -1 if not cached.
     */
    private long cached;
}