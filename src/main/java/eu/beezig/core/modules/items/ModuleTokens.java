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

package eu.beezig.core.modules.items;

import eu.beezig.core.modules.Modules;
import eu.beezig.core.server.ServerHive;
import eu.beezig.core.util.text.Message;
import eu.the5zig.mod.modules.StringItem;

public class ModuleTokens extends StringItem {
    @Override
    protected Object getValue(boolean dummy) {
        return Message.formatNumber(dummy ? 123456 : ServerHive.current().getTokens());
    }

    @Override
    public String getTranslation() {
        return "modules.item.hive_tokens";
    }

    @Override
    public boolean shouldRender(boolean dummy) {
        return dummy || Modules.render();
    }
}
