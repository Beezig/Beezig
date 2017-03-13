/*
 *    Copyright 2016 5zig
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.the5zig.mod.server;

/**
 * An abstract class that represents a server game mode. Override to store custom values for each specific game mode.
 */
public abstract class GameMode {

	/**
	 * Stores a unix time stamp that is either used to display the remaining time or current time, depending
	 * on the {@link #state}.
	 */
	protected long time;
	/**
	 * The current game state.
	 */
	protected GameState state;

	// Preset fields.
	protected int kills;
	protected int killStreak;
	protected long killStreakTime;
	protected int killstreakDuration = 1000 * 20;
	protected int deaths;
	protected String winner;
	protected boolean respawnable;

	public GameMode() {
		this.time = -1;
		killStreakTime = -1;
		this.state = GameState.LOBBY;
		this.respawnable = false;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
		if (state == GameState.FINISHED) {
			time = System.currentTimeMillis() - time;
		} else {
			this.time = -1;
		}
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int kills) {
		this.kills = kills;
	}

	public int getKillStreak() {
		if (killStreakTime != -1 && System.currentTimeMillis() - killStreakTime > 0) {
			killStreakTime = -1;
			killStreak = 0;
		}
		return killStreak;
	}

	public void setKillStreak(int killStreak) {
		this.killStreak = killStreak;
		this.killStreakTime = System.currentTimeMillis() + killstreakDuration;
	}

	public int getDeaths() {
		return deaths;
	}

	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public boolean isRespawnable() {
		return respawnable;
	}

	public void setRespawnable(boolean canRespawn) {
		this.respawnable = canRespawn;
	}

	public abstract String getName();

}
