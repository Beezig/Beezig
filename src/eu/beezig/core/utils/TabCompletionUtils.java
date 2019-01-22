package eu.beezig.core.utils;

import java.util.ArrayList;
import java.util.List;

public class TabCompletionUtils {


    public static List<String> matching(String[] args, List<String> toMatch) {
        String arg = args[args.length - 1];
        List<String> tr = new ArrayList<>();
        if(!arg.isEmpty()) {
            for(String s : toMatch) {
                if(s.regionMatches(true, 0, arg, 0, arg.length()))
                    tr.add(s);
            }
        }
        else {
           tr.addAll(toMatch);
        }
        return tr;
    }


}
