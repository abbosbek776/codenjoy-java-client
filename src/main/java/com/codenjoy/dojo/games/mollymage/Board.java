package com.codenjoy.dojo.games.mollymage;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.runner.GameBoard;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.LinkedList;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * The class is a wrapper over the board string
 * coming from the server. Contains a number of
 * inherited methods {@see AbstractBoard},
 * but you can add any methods based on them here.
 */
@GameBoard
public class Board extends AbstractBoard<Element> {

    @Override
    public Element valueOf(char ch) {
        return Element.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) {
        return size - 1 - y;
    }

    @Override
    protected boolean withoutCorners() {
        return true;
    }

    public Element getAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return Element.WALL;
        }
        return super.getAt(x, y);
    }

    public Collection<Point> getBarriers() {
        Collection<Point> all = getGhosts();
        all.addAll(getWalls());
        all.addAll(getPotions());
        all.addAll(getTreasureBoxes());
        all.addAll(getOtherHeroes());

        return removeDuplicates(all);
    }

    @Override
    public String toString() {
        return String.format("%s\n" +
            "Hero at: %s\n" +
            "Other heroes at: %s\n" +
            "Ghosts at: %s\n" +
            "Treasure boxes at: %s\n" +
            "Potions at: %s\n" +
            "Blasts: %s\n" +
            "Expected blasts at: %s",
                boardAsString(),
                getHero(),
                getOtherHeroes(),
                getGhosts(),
                getTreasureBoxes(),
                getPotions(),
                getBlasts(),
                getFutureBlasts());
    }

    public Point getHero() {
        return get(Element.HERO,
                Element.POTION_HERO,
                Element.DEAD_HERO).get(0);
    }

    public Collection<Point> getOtherHeroes() {
        return get(Element.OTHER_HERO,
                Element.OTHER_POTION_HERO,
                Element.OTHER_DEAD_HERO);
    }

    public boolean isGameOver() {
        return !get(Element.DEAD_HERO).isEmpty();
    }

    public Collection<Point> getGhosts() {
        return get(Element.GHOST);
    }

    public Collection<Point> getWalls() {
        return get(Element.WALL);
    }

    public Collection<Point> getTreasureBoxes() {
        return get(Element.TREASURE_BOX);
    }

    public Collection<Point> getPotions() {
        return get(Element.POTION_TIMER_1,
                Element.POTION_TIMER_2,
                Element.POTION_TIMER_3,
                Element.POTION_TIMER_4,
                Element.POTION_TIMER_5,
                Element.POTION_HERO,
                Element.OTHER_POTION_HERO);
    }

    public Collection<Point> getPerks() {
        return get(Element.POTION_COUNT_INCREASE,
                Element.POTION_REMOTE_CONTROL,
                Element.POTION_IMMUNE,
                Element.POTION_BLAST_RADIUS_INCREASE);
    }

    public Collection<Point> getBlasts() {
        return get(Element.BOOM);
    }

    public Collection<Point> getFutureBlasts() {        
        Collection<Point> potions = getPotions();
        Collection<Point> result = new LinkedList<>();
        for (Point potion : potions) {
            result.add(potion);
            // TODO remove duplicate (check same logic inside parent isNear for example)
            result.add(pt(potion.getX() - 1, potion.getY()));
            result.add(pt(potion.getX() + 1, potion.getY()));
            result.add(pt(potion.getX(), potion.getY() - 1));
            result.add(pt(potion.getX(), potion.getY() + 1));
        }
        Collection<Point> result2 = new LinkedList<>();
        for (Point blast : result) {
            if (blast.isOutOf(size) || getWalls().contains(blast)) {
                continue;
            }
            result2.add(blast);
        }
        return removeDuplicates(result2);
    }

    public boolean isBarrierAt(int x, int y) { // TODO remove this method
        return getBarriers().contains(pt(x, y));
    }

    public boolean isBarrierAt(Point point) {
        return isBarrierAt(point.getX(), point.getY());
    }
    
}