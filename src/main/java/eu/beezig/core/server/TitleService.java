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

package eu.beezig.core.server;

import eu.beezig.core.Beezig;
import eu.beezig.core.data.HiveTitle;
import eu.beezig.core.util.CollectionUtils;
import eu.beezig.core.util.text.Message;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class TitleService {
    private HiveTitle[] titles;

    public TitleService(String modeId) throws IOException {
        this.titles = Beezig.get().getData().getTitleManager().getTitles(modeId);
    }

    public Pair<Integer, HiveTitle> getTitle(String api) {
        int index = CollectionUtils.indexOf(titles, title -> api.equals(title.getPlainName()));
        if(index == -1) return null;
        return new ImmutablePair<>(index, titles[index]);
    }

    public HiveTitle getTitleAt(int index) {
        if(index >= titles.length) return null;
        return titles[index];
    }

    public boolean isValid() {
        return titles != null && titles.length > 0;
    }

    public String getToNext(int index, int points) {
        if(getTitleAt(index).getRequiredPoints() == -1) return "Leaderboard Rank";
        if(++index >= titles.length) return "Highest Rank";
        HiveTitle next = getTitleAt(index);
        int delta = next.getRequiredPoints() - points;
        return Beezig.api().translate("msg.nextrank", Message.formatNumber(delta), next.getColoredName());
    }
}
