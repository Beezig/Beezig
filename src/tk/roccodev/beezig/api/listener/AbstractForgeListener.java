package tk.roccodev.beezig.api.listener;

import tk.roccodev.beezig.api.ArrayContainer;

public interface AbstractForgeListener {

    static AbstractForgeListener fromObject(Object from) {
        return new AbstractForgeListener() {
            @Override
            public void onLoad(String pluginVersion, String zigVersion) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "onLoad", String.class, String.class), pluginVersion, zigVersion);
            }

            @Override
            public void onDisplaySettingsGui(Object[] settings) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "onDisplaySettingsGui", Object.class), new ArrayContainer(settings));
            }
        };
    }

    void onLoad(String pluginVersion, String zigVersion);

    void onDisplaySettingsGui(Object[] settings);

}
