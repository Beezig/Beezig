package eu.beezig.core.util.process;

import eu.beezig.core.Beezig;
import eu.beezig.core.config.Settings;
import eu.beezig.core.notification.NotificationManager;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.obs.ObsState;
import eu.beezig.core.util.process.processes.ScreenRecorders;
import eu.beezig.core.util.process.providers.UnixProcessProvider;
import eu.beezig.core.util.process.providers.WindowsProcessProvider;
import eu.beezig.core.util.task.WorldTask;
import eu.beezig.core.util.text.Message;
import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ProcessManager {
    private IProcessProvider provider;
    private Set<IProcess> processes = new HashSet<>();
    private ObsState obsState;

    @SuppressWarnings("FutureReturnValueIgnored")
    public ProcessManager() {
        String os = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if(os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix"))
            this.provider = new UnixProcessProvider();
        else if(os.contains("win")) this.provider = new WindowsProcessProvider();
        Beezig.get().getAsyncExecutor().scheduleAtFixedRate(() -> {
            if(!ServerHive.isCurrent()) return;
            try {
                updateProcesses();
            } catch (IOException e) {
                ExceptionHandler.catchException(e);
            }
        }, 5, 10, TimeUnit.SECONDS);
    }

    public ObsState getObsState() {
        return obsState;
    }

    private void resetObs() {
        if(obsState != null) obsState.cancelTasks();
        obsState = null;
    }

    private void loadObs() {
        obsState = new ObsState();
    }

    public void refreshObsAuth() throws Exception {
        if(obsState != null) obsState.refreshAuthKey();
        else {
            ObsState state = new ObsState();
            if(state.notAuthenticated()) state.refreshAuthKey();
        }
    }

    private void updateProcesses() throws IOException {
        if(provider == null) return;
        List<String> current = provider.getRunningProcesses();
        Set<IProcess> currentProcesses = new HashSet<>();
        recorders: for(ScreenRecorders recorder : ScreenRecorders.values()) {
            for(String alias : recorder.getAliases()) {
                if(current.contains(alias)) {
                    currentProcesses.add(recorder);
                    continue recorders;
                }
            }
            // If no match is found in the current list, check if it was in the previous list. If so, that means the process was closed.
            if(processes.contains(recorder)) onProcessStop(recorder);
        }
        Set<IProcess> added = new HashSet<>(currentProcesses);
        added.removeAll(processes);
        for(IProcess newProc : added) {
            onProcessStart(newProc);
        }
        processes = currentProcesses;
    }

    private void onProcessStart(IProcess process) {
        if(process instanceof ScreenRecorders) {
            if(Settings.RECORD_DND.get().getBoolean()) {
                WorldTask.submit(() -> {
                    Message.info(Beezig.api().translate("msg.record.dnd", Color.accent() + ((ScreenRecorders) process).name() + Color.primary()));
                    Beezig.get().getNotificationManager().setDoNotDisturb(true, NotificationManager.ActivationCause.PROCESS);
                });
            }
            else {
                MessageComponent main = new MessageComponent(Message.infoPrefix() + Beezig.api().translate("msg.record.found",
                        Color.accent() + ((ScreenRecorders) process).name() + Color.primary()) + "\n");
                TextButton enableNow = new TextButton("btn.record_dnd.name", "btn.record_dnd.desc", "§a");
                enableNow.doRunCommand("/dnd on process");
                TextButton enableAlways = new TextButton("btn.record_setting.name", "btn.record_setting.desc", "§e");
                enableAlways.doRunCommand("/bsettings record.dnd true");
                main.getSiblings().add(enableNow);
                main.getSiblings().add(new MessageComponent(" "));
                main.getSiblings().add(enableAlways);
                WorldTask.submit(() -> Beezig.api().messagePlayerComponent(main, false));
            }
            if(process == ScreenRecorders.OBS) loadObs();
        }
    }

    private void onProcessStop(IProcess process) {
        if(process instanceof ScreenRecorders) {
            NotificationManager notificationManager = Beezig.get().getNotificationManager();
            if(notificationManager.isDoNotDisturb() && notificationManager.getDoNotDisturbCause() == NotificationManager.ActivationCause.PROCESS)
                notificationManager.setDoNotDisturb(false, NotificationManager.ActivationCause.PROCESS);
            if(process == ScreenRecorders.OBS) resetObs();
        }
    }
}
