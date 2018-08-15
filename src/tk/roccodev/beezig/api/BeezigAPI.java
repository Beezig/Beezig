package tk.roccodev.beezig.api;

import eu.the5zig.mod.The5zigAPI;
import tk.roccodev.beezig.BeezigMain;
import tk.roccodev.beezig.api.listener.AbstractForgeListener;

public class BeezigAPI {

    private Object registeredListener;
    private AbstractForgeListener listenerImpl;
    private static BeezigAPI instance;

    public void registerListener(Object toRegister) {
        registeredListener = toRegister;
        listenerImpl = AbstractForgeListener.fromObject(registeredListener);
        listenerImpl.onLoad(BeezigMain.BEEZIG_VERSION, The5zigAPI.getAPI().getModVersion());
    }


    public static BeezigAPI get() {
        return new BeezigAPI();
    }

    public boolean isStaffMember() {
        return BeezigMain.isStaffChat();
    }



    public BeezigAPI() {
        instance = this;
    }






}
