package com.blu.imdg.igfs;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.Ignition;
import org.apache.ignite.igfs.IgfsFile;
import org.apache.ignite.igfs.IgfsPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by shamim on 07/09/16.
 */
public class MapReduceResultReader {
    private final static Logger LOGGER = LoggerFactory.getLogger(MapReduceResultReader.class);
    private final static String IGFS_FS_NAME= "igfs";
    public static void main(String... args) {
        if(args.length < 1){
            LOGGER.error("Usages [java -jar chapter-hadoop-spark-1.0-SNAPSHOT.jar MAP_REDUCE_OUTPUT_DIRECTORY_NAME] for example java -jar chapter-hadoop-spark-1.0-SNAPSHOT.jar /myDir/out");
            System.exit(0);
        }
        // Initialize Ignite with default-config.xml from the classpath
        Ignite ignite = Ignition.start("default-config.xml");
        Ignition.setClientMode(true);

        Collection<IgniteFileSystem> fs = ignite.fileSystems();
        // look for any  existing IGFS file system
        for(Iterator ite = fs.iterator(); ite.hasNext();){
            IgniteFileSystem igniteFileSystem = (IgniteFileSystem) ite.next();

            LOGGER.info("IGFS File System name:" + igniteFileSystem.name());
        }

        IgniteFileSystem igfs = ignite.fileSystem(IGFS_FS_NAME);

        // Read from file.
        IgfsPath outputDir = new IgfsPath(args[0]);
        if(!igfs.exists(outputDir)){
            LOGGER.error("Output directory "+ args[0] +" doesn't exists!");
            System.exit(0);
        }

        Collection<IgfsFile> files =  igfs.listFiles(outputDir);

        for(Iterator ite = files.iterator() ; ite.hasNext();){

            IgfsFile maprFile = (IgfsFile) ite.next();
            LOGGER.info("Output file:"+ maprFile);
            // Read only the file part part-r-00000 if exists
            if(maprFile.isFile() && maprFile.path().name().endsWith("part-r-00000")){
                String line="";
                IgfsPath outputFilePart0 = new IgfsPath(args[0]+"/part-r-00000");
                InputStream inFp0 = igfs.open(outputFilePart0);

                BufferedReader reader = new BufferedReader(new InputStreamReader(inFp0));
                try{
                    while ((line = reader.readLine())!= null ){
                        LOGGER.info(line);
                    }
                } catch(IOException e){
                    LOGGER.error(e.getMessage());
                } finally {
                    try {
                        inFp0.close();
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }

            }
        }

    }
}
