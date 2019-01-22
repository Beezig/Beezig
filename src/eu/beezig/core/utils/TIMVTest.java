package eu.beezig.core.utils;

import eu.beezig.core.games.TIMV;
import eu.beezig.core.BeezigMain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TIMVTest {

    public static File file;

    public static void init() throws IOException {
        file = new File(BeezigMain.mcFile + "/timv/testMessages.txt");
        ArrayList<String> bloccs = Files.readAllLines(Paths.get(file.getPath())).stream().collect(Collectors.toCollection(ArrayList::new));

        bloccs.forEach(s -> {
            if (!TIMV.testRequests.contains(s))
                TIMV.testRequests.add(s);
        });

        if (TIMV.testRequests.size() == 0) {
            TIMV.testRequests.addAll(Arrays.asList("{p} go test please", "{p} test please",
                    "{p} pls test", "{p}, would you mind testing?", "{p}, could you test please?",
                    "{p}, please go into the tester", "{p}, I'd appreciate it if you would test",
                    "{p}, how about testing?", "{p}, would you test for me?"));
            PrintWriter wr = new PrintWriter(new FileWriter(file, false));
            TIMV.testRequests.forEach(wr::println);

            wr.flush();
            wr.close();
        }

    }

    public static void add(String s) throws IOException {
        if (TIMV.testRequests.contains(s))
            return;
        TIMV.testRequests.add(s);
        PrintWriter wr = new PrintWriter(new FileWriter(file, true));

        wr.println(s);

        wr.flush();
        wr.close();

    }

    public static void remove(String s) throws IOException {
        if (!TIMV.testRequests.contains(s))
            return;
        TIMV.testRequests.remove(s);

        PrintWriter wr = new PrintWriter(new FileWriter(file, false));

        TIMV.testRequests.forEach(wr::println);

        wr.flush();
        wr.close();
    }

}
