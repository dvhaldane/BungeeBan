package de.vincidev.bungeeban.commands;

import de.vincidev.bungeeban.BungeeBan;
import de.vincidev.bungeeban.util.BungeeBanManager;
import de.vincidev.bungeeban.util.PlayerUtil;
import de.vincidev.bungeeban.util.TimeUnit;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class TempbanCommand extends Command {

    public TempbanCommand(String name) {
        super(name);
    }

    @Override
    public void execute(final CommandSender sender, final String[] args) {
        BungeeCord.getInstance().getScheduler().runAsync(BungeeBan.getInstance(), new Runnable() {
            @Override
            public void run() {// /tempban user time timeunit reason
                if(sender.hasPermission("BungeeBan.TempBan")) {
                    if(args.length >= 4) {
                        String playername = args[0];
                        String reason = "";
                        for (int i = 3; i <= args.length - 1; i++) {
                            reason = reason + args[i] + " ";
                        }
                        UUID uuid = PlayerUtil.getUniqueId(playername);
                        if(uuid != null) {
                            if(!BungeeBanManager.isBanned(uuid)) {
                                try {
                                    long seconds = Integer.parseInt(args[1]);
                                    TimeUnit unit = TimeUnit.getByString(args[2]);
                                    if(unit != null) {
                                        seconds = seconds * unit.getSeconds();
                                        sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.commands.tempban.banned", "{NAME}~" + playername));
                                        BungeeBanManager.ban(uuid, seconds, reason, sender.getName());
                                    } else {
                                        sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.errors.unknown_timeunit"));
                                    }
                                } catch (NumberFormatException e) {
                                    sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.errors.internal_error", "{DETAILS}~" + e.getMessage()));
                                }
                            } else {
                                sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.errors.player_already_banned", "{NAME}~" + playername));
                            }
                        } else {
                            sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.errors.player_uuid_not_found"));
                        }
                    } else {
                        sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.commands.tempban.syntax"));
                    }
                } else {
                    sender.sendMessage(BungeeBan.PREFIX + BungeeBan.getConfigManager().getString("lang.errors.no_permissions"));
                }
            }
        });
    }

}
