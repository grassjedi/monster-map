package questions;

import game.*;
import graph.RecursivePathFinder;
import serialization.DirectedNetworkSerializer;

import java.io.File;
import java.io.IOException;

public class SerializationRead {

    private FileSerializer serializer;
    private MonsterMapGameMap map;

    public SerializationRead(DirectedNetworkSerializer serializer) {
        this.serializer = new FileSerializer(serializer);
    }

    public void printStats() {
        System.out.println(String.format("Graph order: %d", this.map.getNetwork().getOrder()));
        System.out.println(String.format("Graph size: %d", this.map.getNetwork().getSize()));
        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            pathFinder.findOrderAndPrintPathFromEntranceToExit(this.map, new MonsterMapPathComparator());
        }
        catch (Exception e) {
            System.err.println("Failed to perform entrance->exit path analysis");
            e.printStackTrace(System.err);
        }
    }

    public void readMap(File inFile)
    throws IOException, ClassNotFoundException, MonsterMapGameException {
        this.map = new MonsterMapGameMap(this.serializer.readDirectedNetworkFromFile(inFile, MonsterMapGameNodeContent.class));
        System.out.println(String.format("Successfully read %d bytes", inFile.length()));
        this.printStats();
    }

    public static void main(String[] args) {
        System.out.println("Application demonstrating a random game persisted to a file stream");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", SerializationWrite.class.getName()));
            System.exit(1);
        }
        try {
            SerializationRead reader = new SerializationRead(
                    SerializationWrite.constructSerializer("serialization.OptimizedSerialization"));
            File inFile = new File(args[0]);
            reader.readMap(inFile);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate reader instance", e);
        }
    }
}
