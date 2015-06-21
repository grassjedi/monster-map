package game;

import graph.GraphException;

public interface MonsterMapFactory {

    MonsterMapGameMap getMap() throws MonsterMapGameException;

}
