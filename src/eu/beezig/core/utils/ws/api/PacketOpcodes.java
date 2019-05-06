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

package eu.beezig.core.utils.ws.api;

public class PacketOpcodes {
    public static final int C_ERROR = 0xC00;
    public static final int C_ONLINE_USERS = 0xC01;
    public static final int C_REFETCH = 0xC02;
    public static final int C_NEW_ANNOUNCEMENT = 0xC03;
    public static final int C_NEW_REPORT = 0xC04;
    public static final int C_REPORT_CLAIMED = 0xC05;

    public static final int S_IDENTIFICATION = 0x001;
    public static final int S_REQUEST_ONLINE_USERS = 0x002;
    public static final int S_BEEZIGFORGE_LOADED = 0x003;
    public static final int S_CLAIM_REPORT = 0x004;
}
