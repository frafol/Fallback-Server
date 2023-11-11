package me.candiesjar.fallbackserver.connection;

import me.candiesjar.fallbackserver.FallbackServerBungee;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.ServerDisconnectEvent;
import net.md_5.bungee.api.event.ServerKickEvent;
import net.md_5.bungee.connection.DownstreamBridge;
import net.md_5.bungee.netty.ChannelWrapper;

public class ReconnectBridge extends DownstreamBridge {

    private final UserConnection userConnection;
    private final ServerConnection server;
    private final ProxyServer proxyServer;
    private final FallbackServerBungee plugin;

    public ReconnectBridge(ProxyServer bungee, UserConnection userConnection, ServerConnection server, FallbackServerBungee plugin) {
        super(bungee, userConnection, server);
        this.userConnection = userConnection;
        this.server = server;
        this.plugin = plugin;
        this.proxyServer = ProxyServer.getInstance();
    }

    @Override
    public void disconnected(ChannelWrapper channel) {
        String reason = proxyServer.getTranslation("lost_connection");

        server.getInfo().removePlayer(userConnection);

        if (proxyServer.getReconnectHandler() != null) {
            proxyServer.getReconnectHandler().setServer(userConnection);
        }

        if (server.isObsolete()) {
            return;
        }

        server.setObsolete(true);

        if (userConnection.isConnected()) {
            ServerInfo nextServer = userConnection.updateAndGetNextServer(server.getInfo());
            ServerKickEvent serverKickEvent = proxyServer.getPluginManager().callEvent(new ServerKickEvent(userConnection, server.getInfo(), TextComponent.fromLegacyText(reason), nextServer, ServerKickEvent.State.CONNECTED));

            if (serverKickEvent.isCancelled() && serverKickEvent.getCancelServer() != null) {
                teleportToServer();
            }
        }

        ServerDisconnectEvent serverDisconnectEvent = new ServerDisconnectEvent(userConnection, server.getInfo());
        proxyServer.getPluginManager().callEvent(serverDisconnectEvent);
    }

    @Override
    public void exception(Throwable t) {
        String reason = proxyServer.getTranslation("lost_connection");

        if (server.isObsolete()) {
            return;
        }

        server.setObsolete(true);

        ServerInfo nextServer = userConnection.updateAndGetNextServer(server.getInfo());
        ServerKickEvent serverKickEvent = proxyServer.getPluginManager().callEvent(new ServerKickEvent(userConnection, server.getInfo(), TextComponent.fromLegacyText(reason), nextServer, ServerKickEvent.State.CONNECTED));

        if (serverKickEvent.isCancelled() && serverKickEvent.getCancelServer() != null) {
            teleportToServer();
        }

    }
    private void teleportToServer() {
        ServerInfo reconnectServer = plugin.getReconnectServer();
        userConnection.connect(reconnectServer);
    }
}