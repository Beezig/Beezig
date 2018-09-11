package tk.roccodev.beezig.api.listener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassUtils {

    public static Object invokeMethod(Object o, Method m, Object... params) {
        if (m == null) return null;
        try {
            return m.invoke(o, params);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Method findMethod(Class c, String name, Class<?>... params) {
        try {
            return c.getMethod(name, params);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}
