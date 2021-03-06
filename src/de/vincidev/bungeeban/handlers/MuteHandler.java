package de.vincidev.bungeeban.handlers;

import de.vincidev.bungeeban.BungeeBan;
import de.vincidev.bungeeban.events.BungeeMuteEvent;
import de.vincidev.bungeeban.util.BungeeMuteManager;
import de.vincidev.bungeeban.util.PlayerUtil;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteHandler implements Listener {

    @EventHandler
    public void onMute(BungeeMuteEvent e) {
        List<String> messages = BungeeBan.getConfigManager().getStringList("lang.commands.mute.broadcast",
                "{REASON}~" + e.getmuteReason(),
                "{BY}~" + e.getmutedBy(),
                "{NAME}~" + PlayerUtil.getPlayerName(e.getmuted()),
                "{REMAININGTIME}~" + BungeeMuteManager.getRemainingmuteTime(e.getmuted()));
        for(ProxiedPlayer p : BungeeCord.getInstance().getPlayers()) {
            if(p.hasPermission("BungeeBan.Broadcast.Mute")) {
                for(String msg : messages) {
                    p.sendMessage(BungeeBan.PREFIX + msg);
                }
            }
        }
    }

}