/*
 * Copyright (C) 2019 Beezig (RoccoDev, ItsNiklass)
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

package eu.beezig.core.modules.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RenderUtils {

    public static Object INVENTORY_BACKGROUND;
    private static Method color, pushMatrix, popMatrix, translate, renderPotionIcon, scale, enableBlend, bindTexture, getCurrentScreen;
    private static Object vars;

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
