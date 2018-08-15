package tk.roccodev.beezig.api;

public class BeezigAPILoader {

    static {

        try {
            ClassLoader.getSystemClassLoader().loadClass(BeezigAPI.class.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
