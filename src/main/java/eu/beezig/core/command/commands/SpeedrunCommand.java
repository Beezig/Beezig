package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.api.BeezigForge;
import eu.beezig.core.api.SettingInfo;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.speedrun.SplitLibraryLoader;
import eu.beezig.core.speedrun.config.SpeedrunConfig;
import eu.beezig.core.speedrun.config.SpeedrunSerializer;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpeedrunCommand implements Command {
    @Override
    public String getName() {
        return "speedrun";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/drsplit", "/drspeedrun"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        if(args.length == 0 && BeezigForge.isSupported()) {
            SpeedrunConfig cfg = new SpeedrunConfig(null);
            try {
                SpeedrunSerializer.read(cfg);
                List<SettingInfo> settings = new ArrayList<>();
                cfg.putAll(settings);
                WorldTask.submit(() -> BeezigForge.get().openSpeedrunConfig(settings));
            } catch (IOException | ParseException e) {
                Message.error(Message.translate("error.speedrun.config.load"));
                ExceptionHandler.catchException(e, "Speedrun config load");
            }
        } else if(args.length > 0) {
            String mode = args[0];
            if("natives".equalsIgnoreCase(mode) || "native".equalsIgnoreCase(mode)) {
                Message.info(Message.translate("msg.speedrun.libraries.download"));
                SplitLibraryLoader.downloadNatives().thenAcceptAsync(v -> {
                    boolean success = SplitLibraryLoader.loadSpeedrunLibrary(Beezig.get().getBeezigDir());
                    Beezig.get().setIsNativeSpeedrun(success);
                    if(success) Message.info(Message.translate("msg.speedrun.libraries.download.complete"));
                    else Message.error(Message.translate("error.speedrun.libraries"));
                }).exceptionally(e -> {
                    Message.error(Message.translate("error.speedrun.libraries"));
                    ExceptionHandler.catchException(e);
                    return null;
                });
            } else sendUsage("/drsplit (natives)");
        }
        return true;
    }
}
