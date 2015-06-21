package game;

import graph.DirectedNetwork;
import graph.GraphException;
import graph.SimpleDirectedGraph;

import java.util.List;
import java.util.Random;

public class MonsterMapGameMap {

    private DirectedNetwork<MonsterMapGameNodeContent> network;
    private Integer entranceNodeId;
    public MonsterMapGameMap(DirectedNetwork<MonsterMapGameNodeContent> network)
    throws MonsterMapGameException {
        this();
        this.network = network;
        List<Integer> nodesWithValue = network.findNodesWithValue(MonsterMapGameNodeContent.Entrance);
        if(nodesWithValue.size() != 1) {
            throw new MonsterMapGameException(ONE_ENTRANCE_RULE_MSG);
        }
        this.entranceNodeId = nodesWithValue.get(0);
    }

    public MonsterMapGameMap() {
        this.network = new SimpleDirectedGraph<MonsterMapGameNodeContent>(1, 2);
        this.entranceNodeId = null;
    }

    public DirectedNetwork<MonsterMapGameNodeContent> getNetwork() {
        return this.network;
    }

    public Integer getEntranceNodeId() {
        return this.entranceNodeId;
    }

    public NodeReference entrance()
    throws GraphException, MonsterMapGameException {
        if(this.entranceNodeId != null) {
            throw new MonsterMapGameException(ONE_ENTRANCE_RULE_MSG);
        }
        NodeReference reference = new NodeReference();
        reference.nodeId = this.network.addNode(MonsterMapGameNodeContent.Entrance);
        this.entranceNodeId = reference.nodeId;
        return reference;
    }

    public static MonsterMapGameMap createRandomMap(int maxNrNodes)
    throws MonsterMapGameException {
        MonsterMapGameMap gameMap = new MonsterMapGameMap();
        try {
            NodeReference entrance = gameMap.entrance();
            NodeReference current = entrance;
            Random random = new Random();
            int nodeCount = 1;
            int walkLength = 0;
            final int minWalkLength = 3;
            while (nodeCount < maxNrNodes) {
                boolean canAdd = gameMap.getNetwork()
                        .getReachableNodes(current.nodeId).size() < 2;
                if (current == entrance
                        && !canAdd) {
                    break;
                }
                if (!canAdd || (random.nextBoolean()
                        && walkLength >= minWalkLength
                        && current.getPrevious() != null)) {
                    current = current.getPrevious();
                    walkLength--;
                    continue;
                }
                if (walkLength >= minWalkLength
                        && random.nextBoolean()
                        && canAdd
                        && !current.exit) {
                    current.exit();
                    nodeCount++;
                    continue;
                }
                if (canAdd) {
                    MonsterMapGameNodeContent content =
                            MonsterMapGameNodeContent.values()[random.nextInt(3)];
                    current = current.addNode(content);
                    walkLength++;
                    nodeCount++;
                }
            }
        }
        catch(GraphException e) {
            throw new MonsterMapGameException("Failed to initialize random map");
        }
        return gameMap;
    }

    public class NodeReference {

        private Integer nodeId = null;
        private NodeReference previousNode = null;
        private boolean exit = false;
        public NodeReference left(MonsterMapGameNodeContent content)
        throws GraphException {
            return this.addNode(content);
        }

        public NodeReference right(MonsterMapGameNodeContent content)
        throws GraphException {
            return this.addNode(content);
        }

        public NodeReference up(MonsterMapGameNodeContent content)
        throws GraphException {
            return this.addNode(content);
        }

        public NodeReference down(MonsterMapGameNodeContent content)
        throws GraphException {
            return this.addNode(content);
        }

        public void exit()
        throws GraphException {
            this.addNode(MonsterMapGameNodeContent.Exit);
            this.exit = true;
        }

        public NodeReference getPrevious(int steps) {
            NodeReference reference = this;
            for(int i = 0; i < steps; i++) {
                reference = reference.getPrevious();
            }
            return reference;
        }

        public NodeReference getPrevious() {
            if(this.previousNode == null) {
                throw new IllegalStateException("No previous node to which to move");
            }
            return this.previousNode;
        }

        private NodeReference addNode(MonsterMapGameNodeContent content)
        throws GraphException {
            NodeReference result = new NodeReference();
            result.previousNode = this;
            result.nodeId = MonsterMapGameMap.this.network.addNode(content, this.nodeId);
            return result;
        }

    }

    public static final String ONE_ENTRANCE_RULE_MSG = "A game map may have only one entrance";
}
