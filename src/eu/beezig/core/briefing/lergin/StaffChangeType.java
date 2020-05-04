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

package eu.beezig.core.briefing.lergin;

public enum StaffChangeType {


    MODERATOR_REMOVE("§cRetiring Moderators or Helpers"),
    MODERATOR_ADD("§cNew Moderators or Helpers"),
    SENIOR_MODERATOR_REMOVE("§4Retiring Sr. Moderators"),
    SENIOR_MODERATOR_ADD("§4New Sr. Moderators"),
    NECTAR_REMOVE("§6Retiring Builders"),
    NECTAR_ADD("§6New Builders"),
    DEVELOPER_REMOVE("§7Retiring Developers"),
    DEVELOPER_ADD("§7New Developers"),
    OWNER_REMOVE("§eRetiring Owners"),
    OWNER_ADD("§eNew Owners");


    private String display;

    StaffChangeType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }


}
