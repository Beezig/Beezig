package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;

public class MessageOverlayCommand implements Command {
    @Override
    public String getName() {
        return "msg";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/msg", "/message", "/w", "/tell"};
    }

    @Override
    public boolean execute(String[] args) {
        if(args.length > 1 || !ServerHive.isCurrent()) return false;
        if(args.length == 0) Beezig.get().getMessageOverlayManager().reset();
        else Beezig.get().getMessageOverlayManager().follow(args[0]);
        return true;
    }
}
