package me.candiesjar.fallbackserveraddon.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.candiesjar.fallbackserveraddon.FallbackServerAddon;
import org.bukkit.command.CommandMap;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

@UtilityClass
public class Utils {

    public void unregisterEvent(Listener listener) {

        if (listener == null) {
            return;
        }

        HandlerList.unregisterAll(listener);
    }

    @SneakyThrows
    public CommandMap getCommandMap(FallbackServerAddon plugin) {
        Field f = plugin.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
        f.setAccessible(true);
        return (CommandMap) f.get(plugin.getServer().getPluginManager());
    }
}
