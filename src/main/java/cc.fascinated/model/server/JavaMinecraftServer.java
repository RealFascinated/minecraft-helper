package cc.fascinated.model.server;

import cc.fascinated.Main;
import cc.fascinated.common.JavaMinecraftVersion;
import cc.fascinated.common.ServerUtils;
import cc.fascinated.config.Config;
import cc.fascinated.model.mojang.JavaServerStatusToken;
import com.google.gson.annotations.SerializedName;
import lombok.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

/**
 * @author Braydon
 */
@Setter @Getter
public final class JavaMinecraftServer extends MinecraftServer {

    /**
     * The version of the server.
     */
    @NonNull private final Version version;

    /**
     * The favicon of the server.
     */
    private Favicon favicon;

    /**
     * The mods running on this server.
     */
    private ForgeModInfo modInfo;

    /**
     * The mods running on this server.
     * <p>
     *     This is only used for servers
     *     running 1.13 and above.
     * </p>
     */
    private ForgeData forgeData;

    /**
     * The mojang banned status of the server.
     */
    private boolean mojangBanned;

    public JavaMinecraftServer(String hostname, String ip, int port, MOTD motd, Players players,
                               @NonNull Version version, Favicon favicon, ForgeModInfo modInfo, ForgeData forgeData) {
        super(hostname, ip, port, motd, players);
        this.version = version;
        this.favicon = favicon;
        this.modInfo = modInfo;
        this.forgeData = forgeData;
    }

    /**
     * Create a new Java Minecraft server.
     *
     * @param hostname the hostname of the server
     * @param ip the IP address of the server
     * @param port the port of the server
     * @param token the status token
     * @return the Java Minecraft server
     */
    @NonNull
    public static JavaMinecraftServer create(@NonNull String hostname, String ip, int port, @NonNull JavaServerStatusToken token) {
        String motdString = token.getDescription() instanceof String ? (String) token.getDescription() : null;
        if (motdString == null) { // Not a string motd, convert from Json
            motdString = new TextComponent(ComponentSerializer.parse(Main.GSON.toJson(token.getDescription()))).toLegacyText();
        }
        return new JavaMinecraftServer(
                hostname,
                ip,
                port,
                MinecraftServer.MOTD.create(motdString),
                token.getPlayers(),
                token.getVersion().detailedCopy(),
                JavaMinecraftServer.Favicon.create(token.getFavicon(), ServerUtils.getAddress(hostname, port)),
                token.getModInfo(),
                token.getForgeData()
        );
    }

    @AllArgsConstructor @Getter
    public static class Version {
        /**
         * The version name of the server.
         */
        @NonNull
        private final String name;

        /**
         * The server platform.
         */
        private String platform;

        /**
         * The protocol version.
         */
        private final int protocol;

        /**
         * The name of the protocol, null if not found.
         */
        private final String protocolName;

        /**
         * Create a more detailed
         * copy of this object.
         *
         * @return the detailed copy
         */
        @NonNull
        public Version detailedCopy() {
            String platform = null;
            if (name.contains(" ")) { // Parse the server platform
                String[] split = name.split(" ");
                if (split.length == 2) {
                    platform = split[0];
                }
            }
            JavaMinecraftVersion minecraftVersion = JavaMinecraftVersion.byProtocol(protocol);
            return new Version(name, platform, protocol, minecraftVersion == null ? null : minecraftVersion.getName());
        }

    }

    @Getter @AllArgsConstructor
    public static class Favicon {

        /**
         * The raw base64 of the favicon.
         */
        private final String base64;

        /**
         * The url to the favicon.
         */
        private String url;

        /**
         * Create a new favicon for a server.
         *
         * @param base64 the base64 of the favicon
         * @param address the address of the server
         * @return the new favicon
         */
        public static Favicon create(String base64, @NonNull String address) {
            if (base64 == null) { // The server doesn't have a favicon
                return null;
            }
            return new Favicon(base64, Config.INSTANCE.getWebPublicUrl() + "/server/icon/%s".formatted(address));
        }
    }

    /**
     * Forge mod information for a server.
     */
    @AllArgsConstructor @Getter @ToString
    public static class ForgeModInfo {
        /**
         * The type of modded server this is.
         */
        @NonNull private final String type;

        /**
         * The list of mods on this server, null or empty if none.
         */
        private final ForgeMod[] modList;

        /**
         * A forge mod for a server.
         */
        @AllArgsConstructor @Getter @ToString
        private static class ForgeMod {
            /**
             * The id of this mod.
             */
            @NonNull @SerializedName("modid") private final String name;

            /**
             * The version of this mod.
             */
            private final String version;
        }
    }

    @AllArgsConstructor @Getter
    public static class ForgeData {

        /**
         * The list of mod channels on this server, null or empty if none.
         */
        private final Channel[] channels;

        /**
         * The list of mods on this server, null or empty if none.
         */
        private final Mod[] mods;

        @AllArgsConstructor @Getter
        public static class Channel {
            /**
             * The id of this mod channel.
             */
            @NonNull @SerializedName("res") private final String name;

            /**
             * The version of this mod channel.
             */
            private final String version;

            /**
             * Whether this mod channel is required to join.
             */
            private boolean required;
        }

        @AllArgsConstructor @Getter
        public static class Mod {
            /**
             * The id of this mod.
             */
            @NonNull @SerializedName("modId") private final String name;

            /**
             * The version of this mod.
             */
            @SerializedName("modmarker") private final String version;
        }
    }
}