package game;

import graph.NoSuchPathException;
import graph.PathFinder;
import graph.RecursivePathFinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static questions.Utility.printPath;

public class MonsterMapGamePathFinder {

    private PathFinder pathFinder;

    public MonsterMapGamePathFinder() {
        this(new RecursivePathFinder());
    }

    public MonsterMapGamePathFinder(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public List<MonsterMapGamePath> findPathsFromEntranceToExits(MonsterMapGameMap gameGraph)
    throws NoSuchPathException, MonsterMapGameException {
        List<Integer> exitNodes =
                gameGraph.getNetwork().findNodesWithValue(MonsterMapGameNodeContent.Exit);
        List<MonsterMapGamePath> result = new ArrayList<MonsterMapGamePath>();
        for(Integer exitNode : exitNodes) {
            List<Integer> pathElements = this.pathFinder.findPath(
                    gameGraph.getNetwork(),
                    gameGraph.getEntranceNodeId(),
                    exitNode);
            result.add(new MonsterMapGamePath(gameGraph.getNetwork(), pathElements));
        }
        return result;
    }

    public List<MonsterMapGamePath> findAndOrderPathsFromEntranceToExits(
            MonsterMapGameMap gameGraph,
            Comparator<MonsterMapGamePath> comparator)
    throws NoSuchPathException, MonsterMapGameException {
        List<MonsterMapGamePath> paths = findPathsFromEntranceToExits(gameGraph);
        Collections.sort(paths, comparator);
        return paths;
    }

    public List<MonsterMapGamePath> findAndPrintPathFromEntranceToExit(MonsterMapGameMap gameGraph)
    throws NoSuchPathException, MonsterMapGameException {
        List<MonsterMapGamePath> pathsFromEntranceToExits = this.findPathsFromEntranceToExits(gameGraph);
        for(MonsterMapGamePath path : pathsFromEntranceToExits) {
            printPath(gameGraph.getNetwork(), path);
        }
        return pathsFromEntranceToExits;
    }

    public List<MonsterMapGamePath> findOrderAndPrintPathFromEntranceToExit(
            MonsterMapGameMap gameGraph,
            Comparator<MonsterMapGamePath> comaprator)
    throws NoSuchPathException, MonsterMapGameException {
        List<MonsterMapGamePath> pathsFromEntranceToExits = this.findAndOrderPathsFromEntranceToExits(gameGraph, comaprator);
        for(MonsterMapGamePath path : pathsFromEntranceToExits) {
            printPath(gameGraph.getNetwork(), path);
        }
        return pathsFromEntranceToExits;
    }
}
