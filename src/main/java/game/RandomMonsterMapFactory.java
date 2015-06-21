package game;

import graph.GraphException;

public class RandomMonsterMapFactory implements MonsterMapFactory {
    public MonsterMapGameMap getMap()
    throws MonsterMapGameException {
        return MonsterMapGameMap.createRandomMap(100);
    }
}
