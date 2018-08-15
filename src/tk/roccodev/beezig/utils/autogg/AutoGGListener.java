package tk.roccodev.beezig.utils.autogg;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.PayloadEvent;
import eu.the5zig.mod.event.TitleEvent;
import tk.roccodev.beezig.settings.Setting;

public class AutoGGListener {

    @EventHandler
    public void onTitle(TitleEvent evt) {
        if(evt.getTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getTitle(), 1)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if(Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    }
                    else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
        if (evt.getSubTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getSubTitle(), 2)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if(Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    }
                    else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

    @EventHandler
    public void onChat(ChatEvent evt) {
        if(evt.getMessage().equals("§8▍ §b§lParty§8 ▏ §bParticipating in party chat? No.")) {
            Triggers.inParty = false;
        }
        if(evt.getMessage().equals("§8▍ §b§lParty§8 ▏ §aCreated your party.")) {
            Triggers.lastPartyJoined = System.currentTimeMillis();
        }
        if(evt.getMessage().equals("§8▍ §b§lParty§8 ▏ §bParticipating in party chat? Yes.")) {
            Triggers.inParty = true;
        }
        if(evt.getMessage().startsWith("§8▍ §e§lHive§6§lMC§8 ▏§a §bDid this Party violate our Party Rules?")) {
            Triggers.lastPartyJoined = 0;
            Triggers.inParty = false;
        }
        if(evt.getMessage().startsWith("§8▍ §b§lParty§8 ▏ §aJoined")) {
            Triggers.lastPartyJoined = System.currentTimeMillis();
        }
        if(Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getMessage(), 0)){
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    if(Triggers.inParty) {
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                        The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                        The5zigAPI.getAPI().sendPlayerMessage("/p");
                    }
                    else The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

}
