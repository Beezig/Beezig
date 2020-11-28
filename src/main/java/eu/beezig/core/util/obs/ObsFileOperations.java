package eu.beezig.core.util.obs;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;

import java.awt.*;
import java.io.File;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ObsFileOperations {
    private static final Cache<UUID, File> recordings = Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();

    public static void open(UUID uuid) {
        File file = recordings.getIfPresent(uuid);
        if(file == null) return;
        tryOpen(file);
    }

    public static void openFolder(UUID uuid) {
        File file = recordings.getIfPresent(uuid);
        if(file == null) return;
        tryOpen(file.getParentFile());
    }

    public static void delete(UUID uuid) {
        if(Settings.OBS_CONFIRM_DELETE.get().getBoolean()) {
            File file = recordings.getIfPresent(uuid);
            if (file == null) return;
            MessageComponent main = new MessageComponent(Message.infoPrefix() + Beezig.api().translate("msg.obs.file.delete.confirm",
                Color.accent() + file.getName() + Color.primary()) + "\n");
            TextButton delete = new TextButton("btn.obs.file.delete", "btn.obs.file.delete.desc", "§b");
            delete.doRunCommand("/bobs confirmdelete " + uuid);
            TextButton dismiss = new TextButton("btn.daily.ext.dismiss", "btn.daily.ext.dismiss.desc", "§e");
            dismiss.doRunCommand("/bsettings obs_confirm_delete off");
            main.getSiblings().add(new MessageComponent(Message.infoPrefix()));
            main.getSiblings().add(delete);
            main.getSiblings().add(new MessageComponent(" "));
            main.getSiblings().add(dismiss);
            WorldTask.submit(() -> Beezig.api().messagePlayerComponent(main, false));
        } else confirmDelete(uuid);
    }

    public static void confirmDelete(UUID uuid) {
        File file = recordings.getIfPresent(uuid);
        if(file == null) return;
        boolean res = file.delete();
        if(res) Message.info(Message.translate("msg.obs.file.delete"));
        else Message.error(Message.translate("error.obs.file.delete"));
    }

    static void insert(UUID uuid, File file) {
        recordings.put(uuid, file);
    }

    private static void tryOpen(File file) {
        try {
            Desktop.getDesktop().browse(file.toURI());
        } catch (Exception e) {
            Beezig.logger.error(e);
            Message.error(Beezig.api().translate("error.obs.file.open", file.getAbsolutePath()));
        }
    }
}
