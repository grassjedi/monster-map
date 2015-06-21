package questions;

import game.*;
import graph.RecursivePathFinder;
import serialization.DirectedNetworkSerializer;

import java.io.File;
import java.io.IOException;

public class SerializationWrite {

    private FileSerializer serializer;
    private MonsterMapGameMap map;

    public SerializationWrite(
            MonsterMapFactory mapFactory,
            DirectedNetworkSerializer serializer)
    throws MonsterMapGameException {
        this.map = mapFactory.getMap();
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

    public void save(File outFile)
    throws IOException {
        printStats();
        this.serializer.writeDirectedNetworkToFile(outFile, this.map.getNetwork());
        System.out.println(String.format("Successfully wrote %d bytes", outFile.length()));
    }

    public static DirectedNetworkSerializer constructSerializer(String serializerClassName) {
        try {
            Class<?> serializerClass = Class.forName(serializerClassName);
            return (DirectedNetworkSerializer)serializerClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize serializer instance", e);
        }
    }

    private static MonsterMapFactory constructMapFactory(String factoryClassName) {
        try {
            Class<?> serializerClass = Class.forName(factoryClassName);
            return (MonsterMapFactory)serializerClass.newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to initialize map-factory instance", e);
        }
    }

    public static void potentialMain(String[] args) {
        if(args.length != 3) {
            System.out.println("Usage: ");
            System.out.println(String.format("%s <map-factory-class-name> <serializer-class-name> <filename>"));
            System.exit(1);
        }
        try {
            SerializationWrite writer = new SerializationWrite(constructMapFactory(args[0]), constructSerializer(args[1]));
            File outFile = new File(args[2]);
            writer.save(outFile);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate writer instance", e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Application demonstrating a random game persisted to a file stream");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", SerializationWrite.class.getName()));
            System.exit(1);
        }
        try {
            SerializationWrite writer = new SerializationWrite(
                    constructMapFactory("game.RandomMonsterMapFactory"),
                    constructSerializer("serialization.OptimizedSerialization"));
            File outFile = new File(args[0]);
            writer.save(outFile);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to instantiate writer instance", e);
        }
    }
}
