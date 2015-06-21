package serialization;

import graph.DirectedNetwork;

import java.io.*;

public class SimpleJavaSerialization implements DirectedNetworkSerializer {

    public void writeToStream(OutputStream str, DirectedNetwork<?> graph)
    throws IOException {
        new ObjectOutputStream(str).writeObject(graph);
    }

    public <T>
    DirectedNetwork<T> readFromStream(InputStream str, Class<T> graphContentType)
    throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(str);
        return (DirectedNetwork<T>)objectInputStream.readObject();
    }
}
