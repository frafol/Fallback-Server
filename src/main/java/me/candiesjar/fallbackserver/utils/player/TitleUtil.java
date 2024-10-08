package me.candiesjar.fallbackserver.utils.player;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.experimental.UtilityClass;
import me.candiesjar.fallbackserver.enums.VelocityMessages;
import me.candiesjar.fallbackserver.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;

@UtilityClass
public class TitleUtil {

    public void sendTitle(int fadeIn, int stay, int fadeOut, String title, String subTitle, RegisteredServer registeredServer, Player player) {
        Title.Times times = Title.Times.times(Duration.ofSeconds(fadeIn),
                Duration.ofSeconds(stay),
                Duration.ofSeconds(fadeOut));

        Title createdTitle = Title.title(
                Component.text(ChatUtil.color(title)
                        .replace("%server%", registeredServer.getServerInfo().getName())
                        .replace("%dots%", Utils.getDots(0))),
                Component.text(ChatUtil.color(subTitle)
                        .replace("%server%", registeredServer.getServerInfo().getName())
                        .replace("%dots%", Utils.getDots(0))),
                times);

        player.showTitle(createdTitle);
    }

    public void sendReconnectingTitle(int fadeIn, int stay, int dots, VelocityMessages title, VelocityMessages subTitle, Player player) {
        Title.Times times = Title.Times.times(Duration.ofSeconds(fadeIn),
                Duration.ofSeconds(stay),
                Duration.ofSeconds(1));

        Title createdTitle = Title.title(Component.text(ChatUtil.getFormattedString(title)
                        .replace("%dots%", Utils.getDots(dots))),
                Component.text(ChatUtil.getFormattedString(subTitle)), times);

        player.showTitle(createdTitle);
    }

}
