package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SparseIncidenceMatrix {

    private int graphOrder;
    private int nrInts;
    private int matrixSize;
    private Map<Integer, Integer> matrix;

    public SparseIncidenceMatrix(int graphOrder) {
        this.graphOrder = graphOrder;
        this.matrixSize = graphOrder * graphOrder;
        this.nrInts = this.matrixSize / 32;
        if(this.matrixSize % 32 != 0) {
            this.nrInts += 1;
        }
        this.matrix = new HashMap<Integer, Integer>();
    }

    public void setMatrix(Map<Integer, Integer> matrix) {
        this.matrix = matrix;
    }

    public void setIncidence(int node1, int node2) {
        int bitNr = (node2 * this.graphOrder) + node1;
        int intNr = bitNr / 32;
        bitNr = bitNr % 32;
        int value = 0;
        if(this.matrix.containsKey(intNr)) {
            value = this.matrix.get(intNr);
        }
        int mask = 1;
        mask = 1 << (31 - bitNr);
        value |= mask;
        this.matrix.put(intNr, value);
    }

    public int getInt(int index) {
        if(this.matrix.containsKey(index)) {
            return this.matrix.get(index);
        }
        return 0;
    }

    public int getIntSize() {
        return this.nrInts;
    }

    private List<Integer> getColumnIncidence(int x) {
        List<Integer> result = new ArrayList<Integer>();
        int bitIndex = x;
        for(int i = 0; i < this.graphOrder; i++) {
            bitIndex += this.graphOrder;
            int intNr = bitIndex / 32;
            int bitNr = bitIndex % 32;
            int mask = 1 << (31 - bitNr);
            if(this.matrix.containsKey(intNr)) {
                int value = this.matrix.get(intNr);
                if((value & mask) == mask) {
                    result.add(bitIndex / this.graphOrder);
                }
            }
        }
        return result;
    }

    public List<Integer> getIncidentNodes(int nodeIndex) {
        List<Integer> result = this.getColumnIncidence(nodeIndex);
        return result;
    }
}
