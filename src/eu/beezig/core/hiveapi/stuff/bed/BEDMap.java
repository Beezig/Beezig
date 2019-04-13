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

package eu.beezig.core.hiveapi.stuff.bed;

public enum BEDMap {

    BED_CASTLE("Castle"),
    BED_OCEANIC("Oceanic"),
    BED_TRIBAL("Tribal"),
    BED_SANDSTORM("Sandstorm"),
    BED_FACILITY("Facility"),
    BED_ORBIT("Orbit"),
    BED_IGLOO("Igloo"),
    BED_YINYANG("Yin-Yang"),
    BED_ETHEREAL("Ethereal"),
    BED_MORROWLAND("Morrowland"),
    BED_MARIO("Mario"),
    BED_FOOD("Food", true),
    BED_TREASURE_ISLAND("Treasure Island", true),
    BED_CITYSCAPE("Cityscape", true),
    BED_HAUNTED("Haunted", true),
    BED_PRESENT_FACTORY("Present Factory", true),
    BED_STEAMPUNK("Steampunk"),
    BED_RUINS("Ruins", true, true),    //Teams only
    BED_PIRATES("Pirates", true, true),    //Teams only
    BED_FLORAL("Floral", true, true),    //Teams only
    BED_HELL("Hell", true, true),    //Teams only
    BED_PALACE("Palace", true, true), //Teams only
    BED_SPRING("Spring", true, true), //Teams only
    BED_WORLD_EXHIBITION("World Exhibition", true, true), //Teams only
    BED_SLEEPY_HOLLOW("Sleepy Hollow", true, true), //Teams only
    BED_ROME("Rome", true, true), //Teams only
    BED_ELVEN("Elven", true, true),
    BED_REDROCK("Red Rock", true),
    BED_ESSENCE("Essence", true, true),
    BED_CAROUSEL("Carousel", true, true);

    private String displayName;
    private boolean[] exclusiveModes;

    BEDMap(String display, boolean... exclusiveModes) {    // Modes (by length): 0-solo, 1-duo, 2-teams
        this.displayName = display;
        this.exclusiveModes = exclusiveModes;

    }

    public static BEDMap getFromDisplay(String display) {
        if (display.equals("Facilty")) return BED_FACILITY;
        for (BEDMap map : values()) {
            if (map.getDisplayName().equalsIgnoreCase(display)) return map;
        }
        return null;
    }

    public boolean[] getExclusiveModes() {
        return exclusiveModes;
    }

    public String getDisplayName() {
        return displayName;
    }

}
