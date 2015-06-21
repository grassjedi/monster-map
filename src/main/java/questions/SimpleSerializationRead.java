package questions;

import game.MonsterMapGameMap;
import game.MonsterMapGamePathFinder;
import game.MonsterMapGameNodeContent;
import game.MonsterMapPathComparator;
import graph.DirectedNetwork;
import graph.RecursivePathFinder;
import serialization.SimpleJavaSerialization;

import java.io.File;

public class SimpleSerializationRead {

    public static void main(String[] args) {
        System.out.println("Application demonstrating the use of simple java serialization.");
        if(args.length < 1) {
            System.out.println(String.format("Usage: \n %s <filename>", SimpleSerializationRead.class.getName()));
            System.exit(1);
        }
        File inFile = new File(args[0]);
        FileSerializer serializeDeserialize = new FileSerializer(new SimpleJavaSerialization());
        MonsterMapGameMap map;
        try {
            DirectedNetwork<MonsterMapGameNodeContent> network =
                    serializeDeserialize.readDirectedNetworkFromFile(inFile, MonsterMapGameNodeContent.class);
            map = new MonsterMapGameMap(network);
        }
        catch (Exception e) {
            throw new RuntimeException("Failed read to file", e);
        }

        MonsterMapGamePathFinder pathFinder = new MonsterMapGamePathFinder(new RecursivePathFinder());
        try {
            pathFinder.findOrderAndPrintPathFromEntranceToExit(map, new MonsterMapPathComparator());
        }
        catch (Exception e) {
            throw new RuntimeException("Failed to find game map entrance to exit path", e);
        }

        System.out.println(String.format("Graph order: %d", map.getNetwork().getOrder()));
        System.out.println(String.format("Graph size: %d", map.getNetwork().getSize()));
        System.out.println(String.format("Successfully read %d bytes", inFile.length()));
    }
}
