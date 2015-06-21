package questions;

import graph.DirectedNetwork;
import serialization.DirectedNetworkSerializer;
import serialization.SimpleJavaSerialization;

import java.io.*;

public class FileSerializer {

    private DirectedNetworkSerializer serializer;

    public FileSerializer() {
        this(new SimpleJavaSerialization());
    }

    public FileSerializer(DirectedNetworkSerializer serializer) {
        this.serializer = serializer;
    }

    public void writeDirectedNetworkToFile(
            File destination,
            DirectedNetwork<?> network)
    throws IOException {
        if(!destination.exists()) {
            destination.createNewFile();
        }
        OutputStream ostr = null;
        try {
            ostr = new FileOutputStream(destination);
            this.serializer.writeToStream(ostr, network);
        }
        finally {
            Utility.close(ostr);
        }
    }

    public <T> DirectedNetwork<T> readDirectedNetworkFromFile(
            File source,
            Class<T> networkContentDataType)
    throws IOException, ClassNotFoundException {
        InputStream istr = null;
        try {
            istr = new FileInputStream(source);
            return this.serializer.readFromStream(istr, networkContentDataType);
        }
        finally {
            Utility.close(istr);
        }
    }
}
