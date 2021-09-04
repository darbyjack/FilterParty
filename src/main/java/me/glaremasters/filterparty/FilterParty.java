package me.glaremasters.filterparty;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FilterParty extends JavaPlugin implements Listener {
    private final List<Pattern> patterns = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        for (final String pattern : getConfig().getStringList("patterns")) {
            try {
                patterns.add(Pattern.compile(pattern));
            } catch (Exception ex) {
                getLogger().warning("Could not load the following invalid regex: " + pattern);
            }
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(final AsyncPlayerChatEvent event) {
        for (final Pattern pattern : patterns) {
            final Matcher matcher = pattern.matcher(event.getMessage());
            if (matcher.find()) {
                event.getRecipients().clear();
                break;
            }
        }
    }
}
