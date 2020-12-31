/*
 * Copyright (C) 2017-2021 Beezig Team
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

package eu.beezig.core.util.snipe;

import eu.beezig.core.util.text.TextButton;
import eu.the5zig.mod.util.component.MessageComponent;

public class BlockAction {
    private String original;
    private String withFix;

    public BlockAction(String original, String withFix) {
        this.original = original;
        this.withFix = withFix;
    }

    public String getOriginal() {
        return original;
    }

    public String getWithFix() {
        return withFix;
    }

    public MessageComponent getButtons() {
        String originalKey = "btn.snipe.original";
        String fixKey = "btn.snipe.fix";
        TextButton btn1 = new TextButton(originalKey, originalKey, "§c");
        btn1.doRunCommand("/bsay " + original);
        TextButton applyFix = new TextButton(fixKey, fixKey, "§a");
        applyFix.doRunCommand("/bsay " + withFix);
        btn1.getSiblings().add(new MessageComponent(" "));
        btn1.getSiblings().add(applyFix);
        return btn1;
    }
}
