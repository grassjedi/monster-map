package graph;

import game.MonsterMapGameException;
import game.MonsterMapGameMap;
import game.MonsterMapGameNodeContent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import serialization.DirectedNetworkSerializer;
import serialization.SimpleJavaSerialization;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GameGraphTest {

    private MonsterMapGameMap graph = null;

    @Before
    public void initialize_game_graph()
    throws GraphException, MonsterMapGameException {
        this.graph = new MonsterMapGameMap();
        MonsterMapGameMap.NodeReference origin = this.graph.entrance(); //0
        origin = origin
                .left(MonsterMapGameNodeContent.Monster) //1
                .left(MonsterMapGameNodeContent.Monster) //2
                .left(MonsterMapGameNodeContent.Treasure) //3
                .getPrevious(2);
        origin = origin
                .down(MonsterMapGameNodeContent.Nothing) //4
                .down(MonsterMapGameNodeContent.Treasure) //5
                .getPrevious(3);
        origin = origin
                .right(MonsterMapGameNodeContent.Nothing) //6
                .right(MonsterMapGameNodeContent.Monster) //7
                .right(MonsterMapGameNodeContent.Treasure) //8
                .getPrevious();
        origin = origin
                .up(MonsterMapGameNodeContent.Treasure) //9
                .getPrevious(2);
        origin = origin
                .down(MonsterMapGameNodeContent.Monster) //10
                .down(MonsterMapGameNodeContent.Nothing) //11
                .down(MonsterMapGameNodeContent.Monster) //12
                .getPrevious();
        origin = origin
                .right(MonsterMapGameNodeContent.Treasure) //13
                .right(MonsterMapGameNodeContent.Exit); //14
    }

    @Test
    public void test_create_example_graph() {
        checkSampleGraphStructure(this.graph);
    }

    private static void checkSampleGraphStructure(MonsterMapGameMap graph) {
        Assert.assertEquals("Graph order incorrect", 15, graph.getNetwork().getOrder());
        Assert.assertEquals("Graph size incorrect", 14, graph.getNetwork().getSize());

        Assert.assertEquals("Entrance node in degree incorrect", 0, graph.getNetwork().getIncidentNodes(0).size());
        Assert.assertEquals("Entrance node out degree incorrect", 2, graph.getNetwork().getReachableNodes(0).size());
        Assert.assertArrayEquals("Entrance reachable nodes invalid", new Integer[]{1, 6}, graph.getNetwork().getReachableNodes(0).toArray());

        Assert.assertEquals("Exit node in degree incorrect", 1, graph.getNetwork().getIncidentNodes(14).size());
        Assert.assertEquals("Exit node out degree incorrect", 0, graph.getNetwork().getReachableNodes(14).size());

        Assert.assertEquals("Left walk intersection #1 in degree incorrect", 1, graph.getNetwork().getIncidentNodes(1).size());
        Assert.assertEquals("Left walk intersection #1 out degree incorrect", 2, graph.getNetwork().getReachableNodes(1).size());

        Assert.assertEquals("Right walk intersection #1 in degree incorrect", 1, graph.getNetwork().getIncidentNodes(6).size());
        Assert.assertEquals("Right walk intersection #1 out degree incorrect", 2, graph.getNetwork().getReachableNodes(6).size());

    }
    
    @Test
    public void test_graph_simple_serialization()
    throws IOException, ClassNotFoundException, MonsterMapGameException {
        File testFile = File.createTempFile("treasureMap", ".map", null);
        FileOutputStream fos = new FileOutputStream(testFile);
        DirectedNetworkSerializer serializer = new SimpleJavaSerialization();
        serializer.writeToStream(fos, this.graph.getNetwork());
        fos.close();

        FileInputStream fis = new FileInputStream(testFile);
        DirectedNetwork<MonsterMapGameNodeContent> network = serializer.readFromStream(fis, MonsterMapGameNodeContent.class);
        fis.close();
        checkSampleGraphStructure(new MonsterMapGameMap(network));
    }

    @Test
    public void test_find_exit_path()
    throws NoSuchPathException {
        DirectedNetwork<MonsterMapGameNodeContent> network = this.graph.getNetwork();
        int exitNodeId = network.findNodesWithValue(MonsterMapGameNodeContent.Exit).get(0);
        PathFinder pathFinder = new RecursivePathFinder();
        List<Integer> path = pathFinder.findPath(network, this.graph.getEntranceNodeId(), exitNodeId);
        Assert.assertEquals("Path length from entrance to exit incorrect", 6, path.size());
        Assert.assertArrayEquals("Path elements are invalid", new Integer[] {0, 6, 10, 11, 13, 14}, path.toArray(new Integer[0]));
    }
}
