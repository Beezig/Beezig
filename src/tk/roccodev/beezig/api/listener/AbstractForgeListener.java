package tk.roccodev.beezig.api.listener;

public interface AbstractForgeListener {

    public void onLoad(String pluginVersion, String zigVersion);

    public static AbstractForgeListener fromObject(Object from) {
        return new AbstractForgeListener() {
            @Override
            public void onLoad(String pluginVersion, String zigVersion) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "onLoad", String.class, String.class), pluginVersion, zigVersion);
            }
        };
    }

}
