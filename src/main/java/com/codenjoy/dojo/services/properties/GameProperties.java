package com.codenjoy.dojo.services.properties;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.capitalize;

public class GameProperties {

    public static final String INFO_PROPERTIES = "src/main/webapp/resources/${game}/help/info.properties";

    private Properties properties;
    private String canonicalGame;

    public static String get(String base, String game, String name) {
        GameProperties properties = new GameProperties();
        if (!properties.load(base, game)) {
            throw new RuntimeException("Cant load properties file for game: " + game);
        }
        return properties.get(name);
    }

    public boolean load(String base, String game) {
        properties = new Properties();
        canonicalGame = game;

        String classPath = replace(base + INFO_PROPERTIES, canonicalGame)
                            .replace("../src/main/webapp", "");
        if (tryLoadFromClassPath(classPath)) {
            return true;
        }

        String sourcesPath = replace(base + "${game-source}" + INFO_PROPERTIES, canonicalGame);
        boolean success = tryLoadFromSources(sourcesPath);
        if (!success) {
            System.out.printf("Properties file not found in either: \n" +
                    "\t\t'%s'\n" +
                    "\t\t'%s'\n",
                    classPath, sourcesPath);
        }
        return success;
    }

    /**
     * Попытка загрузить properties файл в runtime из claaapath.
     * @param filePath путь к properties файлу.
     * @return true если успех.
     */
    private boolean tryLoadFromClassPath(String filePath) {
        try {
            InputStream stream = getClass().getResourceAsStream(filePath);
            if (stream != null) {
                properties.load(new InputStreamReader(stream, UTF_8));
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    /**
     * Попытка загрузить properties файл из сырцов, т.к. запуск идет из проекта, в чьих
     * депенденсях нет jar и играми.
     * @param filePath путь к properties файлу.
     * @return true если успех.
     */
    private boolean tryLoadFromSources(String filePath) {
        try {
            File file = new File(filePath);
            filePath = file.getAbsolutePath();
            if (file.exists()) {
                properties.load(new FileReader(filePath, UTF_8));
                return true;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public static String getGame(String canonicalGame) {
        return canonicalGame.replaceAll("[-_.]", "");
    }

    public static String replace(String template, String canonicalGame) {
        return template
                .replace("${game}", getGame(canonicalGame))
                .replace("${game-source}", "../games/${game-canonical}/")
                .replace("${game-canonical}", canonicalGame)
                .replace("${game-capitalize}", capitalize(canonicalGame));
    }

    public String get(String name) {
        String key = String.format("game.%s.element.%s", getGame(canonicalGame), name);
        if (!properties.containsKey(key)) {
            key = key.replace(".element.", ".settings.");
        }
        if (!properties.containsKey(key)) {
            throw new RuntimeException(String.format(
                    "Key not found for either element or setting: [name=%s, game=%s]",
                    name, getGame(canonicalGame)));
        }
        return properties.getProperty(key);
    }
}
