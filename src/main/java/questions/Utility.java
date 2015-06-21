package questions;

import game.MonsterMapGameException;
import game.MonsterMapGameMap;
import game.MonsterMapGameNodeContent;
import game.MonsterMapGamePath;
import graph.DirectedNetwork;
import graph.GraphException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class Utility {

    public static void printPath(DirectedNetwork network, MonsterMapGamePath path) {
        System.out.println("-----------------------------------------------------------");
        System.out.println(
                String.format("Path encounter details: monsters: %d, treasures: %s",
                        path.getMonsterCount(), path.getTreasureCount()));
        System.out.println("-----------------------------------------------------------");
        String prefix = "";
        for (Integer node : path.getPath()) {
            System.out.print(prefix);
            System.out.print(String.format("[%d: %s]", node, network.getNodeContent(node).toString()));
            prefix = " => ";
        }
        System.out.println();
        System.out.println("-----------------------<<>>--------------------------------");
        System.out.println();
    }

    public static void close(Object stream) {
        if(stream instanceof InputStream) {
            try {
                ((InputStream) stream).close();
            }
            catch(IOException e) {
                System.err.println("Failed to close input stream");
                e.printStackTrace(System.err);
            }
        }
        else if(stream instanceof OutputStream) {
            try {
                ((OutputStream) stream).close();
            }
            catch(IOException e) {
                System.err.println("Failed to close output stream");
                e.printStackTrace(System.err);
            }
        }
        else if(stream == null) {
            System.err.println("Could not close null instance");
        }
        else {
            System.err.println(String.format("Could not close instance of %s", stream.getClass().getName()));
        }
    }

    public static MonsterMapGameMap createExampleMap()
    throws MonsterMapGameException {
        try {
            MonsterMapGameMap graph = new MonsterMapGameMap();
            MonsterMapGameMap.NodeReference origin = graph.entrance();
            origin = origin
                    .left(MonsterMapGameNodeContent.Monster)
                    .left(MonsterMapGameNodeContent.Monster)
                    .left(MonsterMapGameNodeContent.Treasure)
                    .getPrevious(2);
            origin = origin
                    .down(MonsterMapGameNodeContent.Nothing)
                    .down(MonsterMapGameNodeContent.Treasure)
                    .getPrevious(3);
            origin = origin
                    .right(MonsterMapGameNodeContent.Nothing)
                    .right(MonsterMapGameNodeContent.Monster)
                    .right(MonsterMapGameNodeContent.Treasure)
                    .getPrevious();
            origin = origin
                    .up(MonsterMapGameNodeContent.Treasure)
                    .getPrevious(2);
            origin = origin
                    .down(MonsterMapGameNodeContent.Monster)
                    .down(MonsterMapGameNodeContent.Nothing)
                    .down(MonsterMapGameNodeContent.Monster)
                    .getPrevious();
            origin
                    .right(MonsterMapGameNodeContent.Treasure)
                    .right(MonsterMapGameNodeContent.Exit);
            return graph;
        }
        catch(GraphException e) {
            throw new MonsterMapGameException("Failed to initialize example map", e);
        }
    }

    public static void printMap(DirectedNetwork<?> network) {
        for(int i = 0; i < network.getOrder(); i++) {
            System.out.print(i + ": ");
            String prefix = "";
            for(Integer n : network.getReachableNodes(i)) {
                System.out.print(prefix);
                System.out.print(n);
                prefix = ", ";
            }
            System.out.println();
        }
    }
}
