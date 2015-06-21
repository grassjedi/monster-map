package graph;

import java.util.ArrayList;
import java.util.List;

public class RecursivePathFinder implements PathFinder {

    public List<Integer> findPath(
            DirectedNetwork<?> network,
            int startNode,
            int endNode)
    throws NoSuchPathException {
        List<Integer> path = findPath(null, network, startNode, endNode);
        if(path == null) {
            throw new NoSuchPathException("No path was found");
        }
        return path;
    }

    private static List<Integer> findPath(
            List<Integer> currentPath,
            DirectedNetwork<?> network,
            int startNode, int endNode) {
        if(currentPath == null) {
            currentPath = new ArrayList<Integer>();
        }
        //Check for cycles
        if(currentPath.contains(startNode)) {
            return null;
        }
        currentPath.add(startNode);
        if(startNode == endNode) {
            return currentPath;
        }
        for (Integer nodeId : network.getReachableNodes(startNode)) {
            List<Integer> path = new ArrayList<Integer>(currentPath);
            List<Integer> subPath = findPath(path, network, nodeId, endNode);
            if(subPath != null) {
                return subPath;
            }
        }
        return null;
    }
}
