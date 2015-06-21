package questions;

import game.MonsterMapGameException;
import game.MonsterMapGameMap;
import game.MonsterMapGamePathFinder;
import game.MonsterMapPathComparator;
import graph.RecursivePathFinder;
import serialization.SimpleJavaSerialization;

import java.io.File;
import java.io.IOException;

import static questions.Utility.createExampleMap;

public class SimpleSerializationWrite {

    public static void main(String[] args) {
        System.out.println("Application demonstrating the use of simple java serialization.");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", SimpleSerializationWrite.class.getName()));
            System.exit(1);
        }
        File outFile = new File(args[0]);
        MonsterMapGameMap graph = null;
        try {
            graph = createExampleMap();
        }
        catch (MonsterMapGameException e) {
            throw new RuntimeException("Failed to create example map", e);
        }
        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            pathFinder.findOrderAndPrintPathFromEntranceToExit(graph, new MonsterMapPathComparator());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to find game map entrance to exit path", e);
        }
        FileSerializer serializeDeserialize = new FileSerializer(new SimpleJavaSerialization());
        try {
            serializeDeserialize.writeDirectedNetworkToFile(outFile, graph.getNetwork());
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to write directed network to file", e);
        }

        System.out.println(String.format("Graph order: %d", graph.getNetwork().getOrder()));
        System.out.println(String.format("Graph size: %d", graph.getNetwork().getSize()));
        System.out.println(String.format("Successfully wrote %d bytes", outFile.length()));
    }
}
