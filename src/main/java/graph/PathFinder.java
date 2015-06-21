package graph;

import java.util.List;

public interface PathFinder {

    List<Integer> findPath(DirectedNetwork<?> network,
                               int startNode,
                               int targetNode)
    throws NoSuchPathException;

}
