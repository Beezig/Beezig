/*
 * Copyright (C) 2017-2020 Beezig Team
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleService {
    private HiveTitle[] titles;
    private String modeId;

    public TitleService(String modeId) throws IOException {
        this.modeId = modeId;
        this.titles = Beezig.get().getData().getTitleManager().getTitles(modeId);
        if("timv".equals(modeId)) {
            List<HiveTitle> titles = new ArrayList<>(Arrays.asList(this.titles));
            HiveTitle watson = getTitle("Watson", 200_000).getRight();
            titles.remove(watson);
            for(int i = 0; i <= 20; i++) {
                HiveTitle level = new HiveTitle();
                level.setRequiredPoints(watson.getRequiredPoints() + 100_000 * i);
                level.setPlainName("Watson");
                level.setHumanName(watson.getHumanName());
                level.setExtraData(" " + Integer.toString(i + 1, 10));
                titles.add(titles.size() - 1, level);
            }
            this.titles = titles.toArray(new HiveTitle[0]);
        }
        else if("bed".equals(modeId)) {
            titles = Arrays.copyOf(titles, titles.length + 1);
            HiveTitle top = new HiveTitle();
            top.setRequiredPoints(-1);
            top.setPlainName("Zzzzzz");
            top.setHumanName("§f§l✸ Zzzzzz");
            titles[titles.length - 1] = top;
        }
    }

    public Pair<Integer, HiveTitle> getTitle(String api) {
        return getTitle(api, 0);
    }

    public Pair<Integer, HiveTitle> getTitle(String api, int points) {
        int index = CollectionUtils.indexOf(titles, title -> api.equals(title.getPlainName()));
        if(index == -1) return null;
        HiveTitle title = titles[index];
        if("timv".equals(modeId) && "Watson".equals(title.getPlainName())) {
            int i2 = CollectionUtils.indexOf(titles, t -> api.equals(t.getPlainName()) && (points - title.getRequiredPoints()) < 100_000);
            if(i2 == -1) return new ImmutablePair<>(index, title);
            return new ImmutablePair<>(i2, titles[i2]);
        }
        else if("bed".equals(modeId) && points > title.getRequiredPoints()) {
            // Top rank, currently it shows as "Sleepy 5" in the API
            return new ImmutablePair<>(titles.length - 1, titles[titles.length - 1]);
        }
        return new ImmutablePair<>(index, title);
    }

    public HiveTitle getTitleAt(int index) {
        if(index >= titles.length) return null;
        return titles[index];
    }

    public boolean isValid() {
        return titles != null && titles.length > 0;
    }

    public String getToNext(int index, int points, String mainColor) {
        if(getTitleAt(index).getRequiredPoints() == -1) return "Leaderboard Rank";
        if(++index >= titles.length) return "Highest Rank";
        HiveTitle next = getTitleAt(index);
        int delta = next.getRequiredPoints() - points;
        return Beezig.api().translate("msg.nextrank", mainColor + Message.formatNumber(delta), next.getColoredName() + mainColor);
    }
}
