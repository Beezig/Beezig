package eu.beezig.core.api.listener;

import eu.beezig.core.api.ArrayContainer;

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

            @Override
            public void setActiveGame(String game) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "setActiveGame", String.class), game);
            }

            @Override
            public void registerCommand(Object commandExecutor) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "registerCommand", Object.class), commandExecutor);
            }

            @Override
            public void displayFriendJoin(String player) {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayFriendJoin", String.class), player);
            }

            @Override
            public void displayAutovoteGui() {
                ClassUtils.invokeMethod(from, ClassUtils.findMethod(from.getClass(), "displayAutovoteGui"));
            }
        };
    }

    void onLoad(String pluginVersion, String zigVersion);

    void onDisplaySettingsGui(Object[] settings);

    void setActiveGame(String game);

    void registerCommand(Object commandExecutor);

    void displayFriendJoin(String player);

    void displayAutovoteGui();

}
