package serialization;

import graph.DirectedNetwork;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface DirectedNetworkSerializer {

    void writeToStream(OutputStream str, DirectedNetwork<?> graph)
    throws IOException;

    <T>
    DirectedNetwork<T> readFromStream(InputStream str, Class<T> graphContentType)
    throws IOException, ClassNotFoundException;
}
