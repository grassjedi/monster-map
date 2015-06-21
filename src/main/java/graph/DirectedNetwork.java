package graph;

import java.io.Serializable;
import java.util.List;

public interface DirectedNetwork<T> extends Serializable {

    int addNode(T content, int...incidentNodes) throws GraphException;

    T removeNode(int node) throws GraphException;

    void makeIncident(int node1, int node2) throws GraphException;

    void makeNotIncident(int node1, int node2);

    boolean areIncident(int node1, int node2);

    int getMaxAllowedNodeInDegree();

    int getMaxAllowedNodeOutDegree();

    int getOrder();

    int getSize();

    List<Integer> findNodesWithValue(T value);

    List<Integer> getIncidentNodes(int nodeId);

    List<Integer> getReachableNodes(int nodeId);

    T getNodeContent(int nodeId);

    List<Integer> getAllNodeIds();
}
