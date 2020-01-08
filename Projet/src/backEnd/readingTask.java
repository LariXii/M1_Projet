package backEnd;

import javafx.concurrent.Task;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.BufferedReader;
import java.io.FileReader;

public abstract class readingTask extends Task<Void> {
    protected String fileName;
    protected long sizeFile;

    public readingTask(String file,long size){
        this.fileName = file;
        this.sizeFile = size;
    }

}
