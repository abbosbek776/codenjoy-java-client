package com.codenjoy.dojo.games.mollymage;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
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

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import java.util.stream.Collectors;

/**
 * Author: your name
 * <p>
 * This is your AI algorithm for the game.
 * Implement it at your own discretion.
 * Pay attention to {@link YourSolverTest} - there is
 * a test framework for you.
 */
public class YourSolver implements Solver<Board> {

  private Dice dice;
  private Board board;

  public YourSolver(Dice dice) {
    this.dice = dice;
  }

  @Override
  public String get(Board board) {
    this.board = board;
    if (board.isGameOver()) {
      return "";
    }

    // TODO put your logic here

    var barriers = board.getBarriers();
    var myHero = board.getHero();
    var potions = board.getPotions();
    var nearElements = board.getNear(myHero);

    Direction direction;

    Point leftChange = Direction.LEFT.change(myHero);
    Point rightChange = Direction.RIGHT.change(myHero);
    Point upChange = Direction.UP.change(myHero);
    Point downChange = Direction.DOWN.change(myHero);
    if (barriers.stream().noneMatch(point -> point.equals(leftChange)) && potions.stream().noneMatch(point -> point.getX() ==
        (leftChange.getX()) || point.getY() ==
        (leftChange.getY()))) {
      direction = Direction.LEFT;
    } else if (barriers.stream().noneMatch(point -> point.equals(rightChange)) && potions.stream().noneMatch(point -> point.getX() ==
        (rightChange.getX()) || point.getY() ==
        (rightChange.getY()))) {
      direction = Direction.RIGHT;
    } else if (barriers.stream().noneMatch(point -> point.equals(upChange)) && potions.stream().noneMatch(point -> point.getY() ==
        (upChange.getY()) || point.getX() ==
        (upChange.getX()))) {
      direction = Direction.UP;
    } else if (barriers.stream().noneMatch(point -> point.equals(downChange)) && potions.stream().noneMatch(point -> point.getY() ==
        (downChange.getY()) || point.getX() ==
        (downChange.getX()))) {
      direction = Direction.DOWN;
    } else {
      direction = Direction.STOP;
    }

    /**
     * Hero at: [17,8]
     * Other heroes at: [[1,1], [1,2], [1,3], [2,3], [4,3], [4,13], [5,3], [8,10], [12,5], [12,7], [15,4], [16,4], [16,6], [18,13]]
     * Enemy heroes at: []
     * Ghosts at: [[2,7], [3,6], [5,11], [8,21], [13,3]]
     * Treasure boxes at: [[1,20], [1,21], [2,1], [2,2], [2,14], [3,2], [3,3], [3,10], [3,15], [4,2], [4,16], [4,17], [5,2], [5,14], [5,19], [5,20], [5,21], [7,16], [9,2], [9,18], [9,19], [10,20], [11,5], [11,7], [11,16], [11,18], [11,21], [12,2], [13,9], [14,4], [14,6], [14,9], [14,10], [15,5], [15,6], [15,14], [15,16], [15,21], [16,16], [16,18], [16,20], [16,21], [17,4], [17,16], [18,4], [18,21], [19,5], [19,13], [19,20], [21,5], [21,6], [21,15]]
     * Potions at: [[17,8]]
     * Blasts: []
     * Expected blasts at: []
     * Answer: ACT,DOWN
     */
//    if (nearElements.stream().anyMatch(element -> element.is(Element.GHOST) || element.is(Element.OTHER_HERO) || element.is(Element.TREASURE_BOX))) {
//      return Command.DROP_POTION_THEN_MOVE.apply(direction);
//    }

//    return Command.MOVE.apply(direction);
    return Command.DROP_POTION_THEN_MOVE.apply(direction);
  }

}