package tk.roccodev.beezig.modules.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RenderUtils {

    private static Method color, pushMatrix, popMatrix, translate, renderPotionIcon, scale, enableBlend, bindTexture, getCurrentScreen;
    private static Object vars;
    public static Object INVENTORY_BACKGROUND;


    public static void init() throws Exception {
        Class glutil = Class.forName("eu.the5zig.mod.util.GLUtil");
        color = glutil.getMethod("color", float.class, float.class, float.class, float.class);
        pushMatrix = glutil.getMethod("pushMatrix");
        popMatrix = glutil.getMethod("popMatrix");
        translate = glutil.getMethod("translate", float.class, float.class, float.class);
        scale = glutil.getMethod("scale", float.class, float.class, float.class);
        enableBlend = glutil.getMethod("enableBlend");

        Class the5zigmod = Class.forName("eu.the5zig.mod.The5zigMod");
        INVENTORY_BACKGROUND = the5zigmod.getField("INVENTORY_BACKGROUND").get(null);
        vars = the5zigmod.getMethod("getVars").invoke(null);
        renderPotionIcon = vars.getClass().getMethod("renderPotionIcon", int.class);
        getCurrentScreen = vars.getClass().getMethod("getMinecraftScreen");
        bindTexture = vars.getClass().getMethod("bindTexture", Object.class);

    }

    public static Object getCurrentScreen() {
        try {
            return getCurrentScreen.invoke(vars);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void renderPotionIcon(int potionIcon) {
        try {
            renderPotionIcon.invoke(vars, potionIcon);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void enableBlend() {
        try {
            enableBlend.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void bindTexture(Object o) {
        try {
            bindTexture.invoke(vars, o);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void color(float r, float g, float b, float alpha) {
        try {
            color.invoke(null, r, g, b, alpha);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void pushMatrix() {
        try {
            pushMatrix.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void popMatrix() {
        try {
            popMatrix.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void translate(float x, float y, float z) {
        try {
            translate.invoke(null, x, y, z);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static void scale(float x, float y, float z) {
        try {
            scale.invoke(null, x, y, z);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
