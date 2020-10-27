/*
 * Copyright (C) 2017-2020 Beezig Team
 *
 * This file is part of Beezig.
 *
 * Beezig is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Beezig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Beezig.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.beezig.core.net.session;

import java.lang.reflect.Method;
import java.net.Proxy;

public class The5zigProvider implements ISessionProvider {

    private Proxy proxy;
    private String session;

    public The5zigProvider() throws ReflectiveOperationException {
        Class mod = Class.forName("eu.the5zig.mod.The5zigMod");
        Class vars = Class.forName("eu.the5zig.mod.util.IVariables");
        Class dataManager = Class.forName("eu.the5zig.mod.manager.DataManager");
        Method getProxy = vars.getMethod("getProxy");
        Method getVars = mod.getMethod("getVars");
        Method getDataMgr = mod.getMethod("getDataManager");
        Method getSession = dataManager.getMethod("getSession");

        Object varsInst = getVars.invoke(null);
        Object dataMgrInst = getDataMgr.invoke(null);
        proxy = (Proxy) getProxy.invoke(varsInst);
        session = (String) getSession.invoke(dataMgrInst);
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public String getSessionString() {
        return session;
    }
}
