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

package eu.beezig.core.command;

import eu.beezig.core.Log;
import eu.the5zig.mod.The5zigAPI;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathCommand implements Command {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "math";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"/math"};
    }


    @Override
    public boolean execute(String[] args) {
        if (args.length == 0) {

            The5zigAPI.getAPI().messagePlayer(Log.info + "§bUsage: /math [expression]\n"
                    + "Clueless? More info at §bhttp://roccodev.tk/beezigmath");

            return true;

        }
        new Thread(() -> {
            try {
                StringBuilder sb = new StringBuilder();
                for (String s : args) {
                    sb.append(s).append(" ");
                }
                String expression = sb.toString().trim();
                if (expression.contains("=")) {
                    The5zigAPI.getAPI().messagePlayer(Log.error + "Invalid character in expression.");
                    return; // Prevent variable instantiation
                }
                if (expression.contains("(")) {
                    String splitParentheses[] = expression.split("\\(");
                    if (splitParentheses[0].contains(".")) {
                        String splitSpace[] = splitParentheses[0].split("\\.")[0].split(" ");
                        String className = splitSpace[splitSpace.length - 1];
                        if (!className.isEmpty() && !className.equals("Math")) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "You can only invoke methods from the Math class!");
                            return; // Protection
                        }
                    } else {
                        //No static method, return

                        Character operator = null;
                        try {
                            operator = expression.charAt(expression.indexOf('(') - 1);
                        } catch (StringIndexOutOfBoundsException ignored) {

                        }
                        if (operator != null && !isOperator(operator)) {
                            The5zigAPI.getAPI().messagePlayer(Log.error + "You can only invoke methods from the Math class!");
                            return; // Protection
                        }
                    }
                }
                ScriptEngineManager sem = new ScriptEngineManager(null);
                ScriptEngine engine = sem.getEngineByName("JavaScript");

                The5zigAPI.getAPI().messagePlayer(Log.info + "Result: §b" + engine.eval(expression));
            } catch (ScriptException ex) {
                The5zigAPI.getAPI().messagePlayer(Log.error + "Error while calculating.");
            }
        }).start();
        return true;
    }


    private boolean isOperator(char sign) {
        if (sign == '-') return true;
        if (sign == '+') return true;
        if (sign == '/') return true;
        return sign == '*';
    }


}
