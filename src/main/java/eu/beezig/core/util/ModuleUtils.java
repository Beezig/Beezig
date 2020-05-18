/*
 * Copyright (C) 2019 Beezig Team
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

package eu.beezig.core.util;

import eu.beezig.core.Beezig;
import eu.the5zig.mod.modules.AbstractModuleItem;
import eu.the5zig.mod.modules.ModuleItemFormatting;

public class ModuleUtils {
    public static String getTextFormatting(AbstractModuleItem item) {
        ModuleItemFormatting fmt = item.getProperties().getFormatting();
        String mainFmt = Beezig.api().getFormatting().getMainFormatting();
        if(fmt != null) {
            if (fmt.getMainColor() != null && fmt.getMainFormatting() == null)
                return mainFmt.replace(mainFmt.charAt(1), fmt.getMainColor().toString().charAt(1));
            else if (fmt.getMainColor() == null && fmt.getMainFormatting() != null)
                return mainFmt.replace(mainFmt.charAt(3), fmt.getMainFormatting().toString().charAt(3));
            else if (fmt.getMainColor() != null && fmt.getMainFormatting() != null)
                return fmt.getMainColor().toString() + fmt.getMainFormatting();
        }
        return mainFmt;
    }
}
