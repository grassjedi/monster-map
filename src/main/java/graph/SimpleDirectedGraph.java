package graph;

import java.io.Serializable;
import java.util.*;

public class SimpleDirectedGraph<T> implements DirectedNetwork<T>, Serializable {
    private Map<Integer, DirectedGraphNode<T>> nodesById;
    private int nodeMaxInDegree, nodeMaxOutDegree;
    private int nextId = 0;

    public SimpleDirectedGraph() {
        this.nodeMaxInDegree = Integer.MAX_VALUE;
        this.nodeMaxOutDegree = Integer.MAX_VALUE;
        this.nodesById = new HashMap<Integer, DirectedGraphNode<T>>();
    }

    public SimpleDirectedGraph(int nodeMaxInDegree, int nodeMaxOutDegree) {
        this();
        this.nodeMaxOutDegree = nodeMaxOutDegree;
        this.nodeMaxInDegree = nodeMaxInDegree;
    }

    public synchronized int addNode(T content, int...incidentNodes)
    throws GraphException {
        DirectedGraphNode newNode = new DirectedGraphNode(this, this.getNextId(), content);
        this.addNode(newNode);
        List<Integer> undoList = new ArrayList<Integer>();
        try {
            if (incidentNodes != null) {
                for (int n : incidentNodes) {
                    this.makeIncident(n, newNode.getId());
                    undoList.add(n);
                }
            }
        }
        catch(GraphException e) {
            for(Integer n : undoList) {
                this.makeNotIncident(newNode.getId(), n);
            }
            this.removeNode(newNode.getId());
            throw e;
        }

        return newNode.getId();
    }

    private void addNode(DirectedGraphNode<T> node) {
        this.nodesById.put(node.getId(), node);
    }

    public synchronized T removeNode(int nodeId)
    throws GraphException {
        DirectedGraphNode<T> nodeToRemove = this.getNodeById(nodeId);
        for(Integer n : nodeToRemove.getEntranceNodes()) {
            this.nodesById.get(n).removeExitNode(nodeToRemove.getId());
        }
        for(Integer n : nodeToRemove.getExitNodes()) {
            this.nodesById.get(n).removeEntranceNode(nodeToRemove.getId());
        }
        this.removeNode(nodeToRemove);
        return nodeToRemove.getContent();
    }

    private void removeNode(DirectedGraphNode<T> node) {
        DirectedGraphNode<T> nodeRemoved = this.nodesById.remove(node.getId());
        if(nodeRemoved == null) {
            throw new NoSuchElementException(String.format(NODE_NOT_FOUND, node.getId()));
        }
    }

    private DirectedGraphNode<T> getNodeById(int nodeId) {
        DirectedGraphNode<T> node = this.nodesById.get(nodeId);
        if(node == null) {
            throw new NoSuchElementException(String.format(NODE_NOT_FOUND, nodeId));
        }
        return node;
    }

    public synchronized void makeIncident(int node1Id, int node2Id)
    throws GraphException {
        DirectedGraphNode<T> node1 = this.getNodeById(node1Id);
        DirectedGraphNode<T> node2 = this.getNodeById(node2Id);
        node1.addExitNode(node2Id);
        node2.addEntranceNode(node1Id);
    }

    public synchronized void makeNotIncident(int node1Id, int node2Id) {
        DirectedGraphNode<T> node1 = this.getNodeById(node1Id);
        DirectedGraphNode<T> node2 = this.getNodeById(node2Id);
        node1.removeExitNode(node2Id);
        node2.removeEntranceNode(node1Id);
    }

    public synchronized boolean areIncident(int node1Id, int node2Id) {
        DirectedGraphNode<T> node1 = null;
        DirectedGraphNode<T> node2 = null;
        try {
            node1 = this.getNodeById(node1Id);
            node2 = this.getNodeById(node2Id);
        }
        catch(NoSuchElementException e) {
            return false;
        }
        for(Integer n : node2.getEntranceNodes()) {
            if(n == node1Id) {
                return true;
            }
        }
        return false;
    }

    public synchronized int getOrder() {
        return this.nodesById.size();
    }

    public synchronized int getSize() {
        int accumulator = 0;
        for(DirectedGraphNode n : this.nodesById.values()) {
            accumulator += n.getInDegree();
            accumulator += n.getOutDegree();
        }
        return accumulator / 2;
    }

    public synchronized List<Integer> findNodesWithValue(T value) {
        List<Integer> graphNodes = new ArrayList<Integer>();
        for(DirectedGraphNode graphNode : this.nodesById.values()) {
            if((value == null && graphNode.getContent() == null)) {
                graphNodes.add(graphNode.getId());
            }
            else if(value != null && graphNode.getContent() != null
                    && value.equals(graphNode.getContent())) {
                graphNodes.add(graphNode.getId());
            }
        }
        return graphNodes;
    }

    public synchronized T getNodeContent(int nodeId) {
        DirectedGraphNode<T> node = this.getNodeById(nodeId);
        return node.getContent();
    }

    public synchronized List<Integer> getIncidentNodes(int nodeId) {
        List<Integer> graphNodes = new ArrayList<Integer>();
        graphNodes.addAll(this.getNodeById(nodeId).getEntranceNodes());
        return graphNodes;
    }

    public synchronized List<Integer> getReachableNodes(int nodeId) {
        List<Integer> graphNodes = new ArrayList<Integer>();
        graphNodes.addAll(this.getNodeById(nodeId).getExitNodes());
        return graphNodes;
    }

    public synchronized int getMaxAllowedNodeInDegree() {
        return this.nodeMaxInDegree;
    }

    public synchronized int getMaxAllowedNodeOutDegree() {
        return this.nodeMaxOutDegree;
    }

    private synchronized int getNextId() {
        return this.nextId++;
    }

    public synchronized List<Integer> getAllNodeIds() {
        ArrayList<Integer> list = new ArrayList<Integer>(this.nodesById.keySet());
        Collections.sort(list);
        return Collections.unmodifiableList(list);
    }

    private static final String NODE_NOT_FOUND = "Could not find node identified by \"%d\"";

    private static final long serialVersionUID = 1L;
}
