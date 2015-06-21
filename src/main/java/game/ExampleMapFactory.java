package game;

import graph.GraphException;
import questions.Utility;

public class ExampleMapFactory implements MonsterMapFactory {
    public MonsterMapGameMap getMap() throws MonsterMapGameException {
        return Utility.createExampleMap();
    }
}
