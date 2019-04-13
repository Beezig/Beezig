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

package eu.beezig.core.utils.soundcloud;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class TrackDownloader {

    public static BufferedInputStream trackStream(String trackId) throws IOException {
        URL trackUrl = new URL("http://api.soundcloud.com/tracks/" + trackId + "/stream?client_id=b91f180fff0ec5cd1d4b0dcab8d59967");
        HttpURLConnection conn = (HttpURLConnection) trackUrl.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();

        String redirect = map.get("Location").get(0);
        URL newUrl = new URL(redirect);

        return new BufferedInputStream(newUrl.openStream());

    }


}
