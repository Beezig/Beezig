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

package eu.beezig.core.hiveapi.stuff.gnt;

public enum GiantMap {

    //GNT Maps

    FORTRESS("Fortress", false),
    LOST("Lost", false),
    ELYSIUM("Elysium", false),
    TROPICAL_TROUBLE("Tropical Trouble", false),
    LUMBER("Lumber", false),
    ORC("Orc", false),
    MAGIKOS("Magikos", false),
    ODYSSEY("Odyssey", false),
    GATEWAY("Gateway", false),
    AIRSHIP("Airship", false),
    RADIOACTIVE("Radioactive", false),
    DRAGONS("Dragons", false),
    ADVENUS("Advenus", false),
    MOONLIGHT("Moonlight", false),
    ANCHORED("Anchored", false),
    ANUBIS("Anubis", false),
    SKY_KINGDOM("Sky Kingdom", false),
    STRONGHOLD("Stronghold", false),
    SNOWFIGHT("Snow Fight", false),


    //GNTM Maps

    SANCTUM("Sanctum", true),
    M_FORTRESS("Fortress", true),
    OASIS("Oasis", true),
    EVERGREEN("Evergreen", true),
    BABYLON("Babylon", true),
    URBAN("Urban", true),
    RED("Red", true),
    SPRUCE_SPRINGSTEEN("Spruce Springsteen", true),
    BLOSSOM("Blossom", true),
    TOWN_HALL("Town Hall", true),
    MUFFIN("Muffin", true),
    AQUARIUS("Aquarius", true),
    GWOLITH("Gwolith", true),
    ROCKET("Rocket", true),
    WITCHSLAIR("Witch's Lair", true);


    private String display;
    private boolean mini;

    GiantMap(String display, boolean mini) {

        this.display = display;
        this.mini = mini;

    }

    public static GiantMap get(String display, boolean mini) {
        for (GiantMap map : values()) {
            if (map.isMini() == mini) {
                if (map.getDisplay().equalsIgnoreCase(display)) return map;
            }
        }
        return null;
    }

    public String getDisplay() {
        return display;
    }

    public boolean isMini() {
        return mini;
    }


}
