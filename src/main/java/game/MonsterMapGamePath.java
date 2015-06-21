package game;

import graph.DirectedNetwork;

import java.util.Collections;
import java.util.List;

public class MonsterMapGamePath {
    private int nothingCount, treasureCount, monsterCount;
    private List<Integer> path;

    public MonsterMapGamePath(
            DirectedNetwork<MonsterMapGameNodeContent> directedNetwork,
            List<Integer> pathNodes)
    throws MonsterMapGameException {
        if(pathNodes.size() < 2) {
            throw new MonsterMapGameException("A path must contain at least 2 nodes");
        }
        if(directedNetwork.getNodeContent(pathNodes.get(0))
                != MonsterMapGameNodeContent.Entrance
           || directedNetwork.getNodeContent(pathNodes.get(pathNodes.size() -1))
                != MonsterMapGameNodeContent.Exit) {
            throw new MonsterMapGameException("A valid path must start at an entrance and end at an exit");
        }
        this.path = pathNodes;
        this.scorePath(directedNetwork, pathNodes);
    }

    private void scorePath(
            DirectedNetwork<MonsterMapGameNodeContent> directedNetwork,
            List<Integer> pathNodes) {
        for(Integer node : pathNodes) {
            switch(directedNetwork.getNodeContent(node)) {
                case Nothing:
                    this.nothingCount++;
                    break;
                case Treasure:
                    this.treasureCount++;
                    break;
                case Monster:
                    this.monsterCount++;
                    break;
                default:
                    break;
            }
        }
    }

    public int getNothingCount() {
        return this.nothingCount;
    }

    public int getTreasureCount() {
        return this.treasureCount;
    }

    public int getMonsterCount() {
        return this.monsterCount;
    }

    public List<Integer> getPath() {
        return Collections.unmodifiableList(this.path);
    }
}
