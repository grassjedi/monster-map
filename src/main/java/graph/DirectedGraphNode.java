package graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectedGraphNode<T> implements Comparable<DirectedGraphNode>, Serializable {

    private int id;
    private T content;
    private List<Integer> entranceNodes, exitNodes;
    private transient DirectedNetwork graph;

    protected DirectedGraphNode(DirectedNetwork graph, int id, T content) {
        this.id = id;
        this.content = content;
        this.entranceNodes = new ArrayList<Integer>();
        this.exitNodes = new ArrayList<Integer>();
        this.graph = graph;
    }

    private static boolean addIncidentNode(String degreeType, List<Integer> nodes, int maxDegree, int nodeId)
    throws GraphException {
        for(Integer n : nodes) {
            if(n == nodeId) {
                //the current node is already entered by this node
                return false;
            }
        }
        if(nodes.size() >= maxDegree) {
            throw new GraphException(String.format("%s may not exceed %d", degreeType, maxDegree));
        }
        nodes.add(nodeId);
        Collections.sort(nodes);
        return true;
    }

    protected boolean addEntranceNode(int nodeId)
    throws GraphException {
        return addIncidentNode("In-degree", this.entranceNodes, this.graph.getMaxAllowedNodeInDegree(), nodeId);
    }

    protected boolean addExitNode(int nodeId)
    throws GraphException {
        return addIncidentNode("Out-degree", this.exitNodes, this.graph.getMaxAllowedNodeOutDegree(), nodeId);
    }

    private static boolean removeIncidentNode(List<Integer> nodes, int nodeId) {
        Integer nodeRemoved = null;
        for(int i = 0; i < nodes.size(); i++) {
            if(nodes.get(i) == nodeId) {
                nodeRemoved = nodes.remove(i);
                break;
            }
        }
        return nodeRemoved != null;
    }
    
    protected boolean removeEntranceNode(int nodeId) {
        return this.removeIncidentNode(this.entranceNodes, nodeId);
    }

    protected boolean removeExitNode(int nodeId) {
        return this.removeIncidentNode(this.exitNodes, nodeId);
    }

    protected int getId() {
        return this.id;
    }

    protected void setContent(T content) {
        this.content = content;
    }

    protected T getContent() {
        return this.content;
    }

    protected int getOutDegree() {
        return this.exitNodes.size();
    }

    protected int getInDegree() {
        return this.entranceNodes.size();
    }

    protected List<Integer> getEntranceNodes() {
        return Collections.unmodifiableList(this.entranceNodes);
    }

    protected List<Integer> getExitNodes() {
        return Collections.unmodifiableList(this.exitNodes);
    }

    public final String toString() {
        return String.format("%s:[%d]", this.getClass().getName(), this.getId());
    }

    public final int hashCode() {
        return this.toString().hashCode();
    }

    public final boolean equals(Object other) {
        return other != null && other instanceof DirectedGraphNode && this.getId() == ((DirectedGraphNode) other).getId();
    }

    public final int compareTo(DirectedGraphNode o) {
        return new Integer(this.getId()).compareTo(o.getId());
    }

    private static final long serialVersionUID = 1L;
}
