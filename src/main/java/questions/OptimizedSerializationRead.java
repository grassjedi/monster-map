package questions;

import game.*;
import graph.DirectedNetwork;
import graph.NoSuchPathException;
import graph.RecursivePathFinder;
import serialization.OptimizedSerialization;

import java.io.File;

public class OptimizedSerializationRead {

    public static void main(String[] args) {
        System.out.println("Application demonstrating the use of custom serialization.");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", OptimizedSerializationRead.class.getName()));
            System.exit(1);
        }
        File inFile = new File(args[0]);
        FileSerializer serializeDeserialize = new FileSerializer(new OptimizedSerialization());
        DirectedNetwork<MonsterMapGameNodeContent> network;
        try {
            network = serializeDeserialize.readDirectedNetworkFromFile(inFile, MonsterMapGameNodeContent.class);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed read to file", e);
        }

        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            MonsterMapGameMap gameMap = new MonsterMapGameMap(network);
            pathFinder.findOrderAndPrintPathFromEntranceToExit(gameMap, new MonsterMapPathComparator());
        }
        catch (MonsterMapGameException e) {
            throw new RuntimeException("Failed to create game-map from directed-network", e);
        }
        catch (NoSuchPathException e) {
            throw new RuntimeException("Failed to find game map entrance to exit path", e);
        }

        if(network != null) {
            System.out.println(String.format("Graph order: %d", network.getOrder()));
            System.out.println(String.format("Graph size: %d", network.getSize()));
            System.out.println(String.format("Successfully read %d bytes", inFile.length()));
        }
    }
}
