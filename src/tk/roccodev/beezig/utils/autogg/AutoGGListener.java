package tk.roccodev.beezig.utils.autogg;

import eu.the5zig.mod.The5zigAPI;
import eu.the5zig.mod.event.ChatEvent;
import eu.the5zig.mod.event.EventHandler;
import eu.the5zig.mod.event.TitleEvent;
import tk.roccodev.beezig.settings.Setting;

public class AutoGGListener {

    @EventHandler
    public void onTitle(TitleEvent evt) {
        if(evt.getTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getTitle(), 1)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
        if (evt.getSubTitle() != null && Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getSubTitle(), 2)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

    @EventHandler
    public void onChat(ChatEvent evt) {
        if(Setting.AUTOGG.getValue() && Triggers.enabled && Triggers.shouldGG(evt.getMessage(), 0)){
            new Thread(() -> {
                try {
                    Thread.sleep(Triggers.delay);
                    The5zigAPI.getAPI().sendPlayerMessage(Triggers.ggText);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }

}
