package questions;

import game.MonsterMapGameException;
import game.MonsterMapGameMap;
import game.MonsterMapGamePathFinder;
import graph.NoSuchPathException;
import graph.RecursivePathFinder;

public class FindPathInExampleMap {
    public static void main(String[] args) {
        MonsterMapGameMap gameMap;
        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            gameMap = Utility.createExampleMap();
            pathFinder.findAndPrintPathFromEntranceToExit(gameMap);
        }
        catch (MonsterMapGameException e) {
            throw new RuntimeException("Failed to create example map", e);
        }
        catch (NoSuchPathException e) {
            throw new RuntimeException("Failed to find path");
        }
    }
}
