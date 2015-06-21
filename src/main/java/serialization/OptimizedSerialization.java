package serialization;

import graph.DirectedNetwork;
import graph.GraphException;
import graph.SimpleDirectedGraph;
import graph.SparseIncidenceMatrix;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptimizedSerialization implements DirectedNetworkSerializer {

    private SparseIncidenceMatrix serializeOutOrder(
            List<Integer> nodeIds,
            DirectedNetwork<?> graph) {
        SparseIncidenceMatrix matrix = new SparseIncidenceMatrix(graph.getOrder());
        for(Integer i : nodeIds) {
            for(Integer j : graph.getReachableNodes(i)) {
                matrix.setIncidence(i, j);
            }
        }
        return matrix;
    }

    public void writeToStream(
            OutputStream str,
            DirectedNetwork<?> graph)
    throws IOException {
        ObjectOutputStream ostr = new ObjectOutputStream(str);
        List<Integer> allNodeIds = graph.getAllNodeIds();
        SparseIncidenceMatrix matrix = this.serializeOutOrder(allNodeIds, graph);
        ostr.writeInt(graph.getOrder());
        for(int i = 0; i < matrix.getIntSize(); i++) {
            ostr.writeInt(matrix.getInt(i));
        }
        for(Integer n : allNodeIds) {
            ostr.writeObject(graph.getNodeContent(n));
        }
    }

    public <T> DirectedNetwork<T> readFromStream(
            InputStream str,
            Class<T> graphContentType)
    throws IOException, ClassNotFoundException {
        ObjectInputStream istr = new ObjectInputStream(str);
        int graphOrder = istr.readInt();
        SparseIncidenceMatrix matrix = new SparseIncidenceMatrix(graphOrder);
        Map<Integer, Integer> sparseMatrixMap = new HashMap<Integer, Integer>();
        for(int i = 0; i < matrix.getIntSize(); i++) {
            int value = istr.readInt();
            if(value != 0) {
                sparseMatrixMap.put(i, value);
            }
        }
        matrix.setMatrix(sparseMatrixMap);
        SimpleDirectedGraph<T> graph = new SimpleDirectedGraph<T>();
        for(int i = 0; i < graphOrder; i++) {
            T value = (T)istr.readObject();
            try {
                graph.addNode(value);
            }
            catch (GraphException e) {
                //No incidence operations means no exceptions expected
                throw new RuntimeException("Failed to deserialize graph");
            }
        }
        for(int i = 0; i < graphOrder; i++) {
            List<Integer> incidentNodes = matrix.getIncidentNodes(i);
            for(Integer j : incidentNodes) {
                try {
                    graph.makeIncident(i, j);
                }
                catch (GraphException e) {
                    //Nodes have all been added means no exceptions expected
                    throw new RuntimeException("Failed to deserialize graph");
                }
            }
        }
        return graph;
    }
}
