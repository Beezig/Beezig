package tk.roccodev.zta.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import tk.roccodev.zta.ZTAMain;
import tk.roccodev.zta.games.TIMV;

public class TIMVTest {

	public static File file;

	public static void init() throws IOException {
		file = new File(ZTAMain.mcFile + "/timv/testMessages.txt");
		ArrayList<String> bloccs = new ArrayList<String>(
				Files.readAllLines(Paths.get(file.getPath())).stream().collect(Collectors.toList()));

		bloccs.forEach(s -> {
			if (!TIMV.testRequests.contains(s))
				TIMV.testRequests.add(s);
		});

		if (TIMV.testRequests.size() == 0) {
			TIMV.testRequests.addAll(Arrays.asList(new String[] { "{p} go test please", "{p} test please",
					"{p} pls test", "{p}, would you mind testing?", "{p}, could you test please?",
					"{p}, please go into the tester", "{p}, I'd appreciate it if you would test",
					"{p}, how about testing?", "{p}, would you test for me?"

			}));
			PrintWriter wr = new PrintWriter(new FileWriter(file, false));
			TIMV.testRequests.forEach(s1 -> wr.println(s1));

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

		TIMV.testRequests.forEach(s1 -> wr.println(s1));

		wr.flush();
		wr.close();
	}

}
