package questions;

import game.MonsterMapGameMap;
import game.MonsterMapGamePathFinder;
import game.MonsterMapPathComparator;
import graph.RecursivePathFinder;
import serialization.OptimizedSerialization;

import java.io.File;
import java.io.IOException;

import static questions.Utility.createExampleMap;

public class OptimizedSerializationWrite {

    public static void main(String[] args) {
        System.out.println("Application demonstrating the use of custom serialization.");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", OptimizedSerializationWrite.class.getName()));
            System.exit(1);
        }
        MonsterMapGameMap graph;
        try {
            graph = createExampleMap();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to create example map", e);
        }
        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            pathFinder.findOrderAndPrintPathFromEntranceToExit(graph, new MonsterMapPathComparator());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to find game map entrance to exit path", e);
        }
        FileSerializer serializeDeserialize = new FileSerializer(new OptimizedSerialization());

        File outFile = new File(args[0]);
        try {
            serializeDeserialize.writeDirectedNetworkToFile(outFile, graph.getNetwork());
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write map to stream", e);
        }
        System.out.println(String.format("Graph order: %d", graph.getNetwork().getOrder()));
        System.out.println(String.format("Graph size: %d", graph.getNetwork().getSize()));
        System.out.println(String.format("Successfully wrote %d bytes", outFile.length()));
    }
}
