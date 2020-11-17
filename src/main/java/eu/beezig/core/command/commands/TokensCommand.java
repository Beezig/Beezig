package eu.beezig.core.command.commands;

import eu.beezig.core.Beezig;
import eu.beezig.core.command.Command;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.Color;
import eu.beezig.core.util.ExceptionHandler;
import eu.beezig.core.util.UUIDUtils;
import eu.beezig.core.util.text.Message;
import eu.beezig.hiveapi.wrapper.exception.ProfileNotFoundException;
import eu.beezig.hiveapi.wrapper.player.Profiles;
import org.json.simple.parser.ParseException;

public class TokensCommand implements Command {
    @Override
    public String getName() {
        return "tokens";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"/tokens"};
    }

    @Override
    public boolean execute(String[] args) {
        if(!ServerHive.isCurrent()) return false;
        String name = args.length == 0 ? Beezig.user().getName() : args[0];
        Profiles.global(args.length == 0 ? UUIDUtils.strip(Beezig.user().getId()) : args[0])
            .thenAcceptAsync(profile -> {
                Message.info(Beezig.api().translate("msg.tokens", UUIDUtils.getNameWithOptionalRank(name, name, profile).join() + Color.primary(), Color.accent()
                    + Message.formatNumber(profile.getTokens()) + Color.primary()));
            }).exceptionally(e -> {
                if(e.getCause() instanceof ProfileNotFoundException || e.getCause() instanceof ParseException) {
                    Message.info(Beezig.api().translate("msg.tokens", Color.accent() + name + Color.primary(),
                        Color.accent() + Message.formatNumber(Math.abs(name.toLowerCase().hashCode() / (name.length() * 10_000)))) + Color.primary());
                } else ExceptionHandler.catchException(e);
                return null;
        });
        return true;
    }
}
