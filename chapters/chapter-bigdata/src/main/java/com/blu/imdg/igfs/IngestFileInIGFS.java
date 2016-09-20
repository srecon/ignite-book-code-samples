package com.blu.imdg.igfs;

import com.google.common.io.ByteStreams;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteFileSystem;
import org.apache.ignite.Ignition;
import org.apache.ignite.igfs.IgfsPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;


/**
 * Created by shamim on 06/09/16.
 */
public class IngestFileInIGFS {
    private final static Logger LOGGER = LoggerFactory.getLogger(IngestFileInIGFS.class);
    private final static String IGFS_FS_NAME= "igfs";

    public static void main(String... args) {
        if(args.length < 2){
            LOGGER.error("Usages [java -jar chapter-hadoop-spark-1.0-SNAPSHOT.jar DIRECTORY_NAME FILE NAME, for example java -jar chapter-hadoop-spark-1.0-SNAPSHOT.jar myDir myFile]");
            System.exit(0);
        }

        Ignite ignite = Ignition.start("default-config.xml");
        Ignition.setClientMode(true);

        Collection<IgniteFileSystem> fs = ignite.fileSystems();

        for(Iterator ite = fs.iterator();ite.hasNext();){
            IgniteFileSystem igniteFileSystem = (IgniteFileSystem) ite.next();

            LOGGER.info("IGFS File System name:" + igniteFileSystem.name());
        }

        IgniteFileSystem igfs = ignite.fileSystem(IGFS_FS_NAME);
        // Create directory.
        IgfsPath dir = new IgfsPath("/" + args[0]);
        igfs.mkdirs(dir);

        // Create file and write some data to it.
        IgfsPath file = new IgfsPath(dir, args[1]);
        // Read the File Shakespeare
        InputStream inputStream = IngestFileInIGFS.class.getClassLoader().getResourceAsStream("t8.shakespeare.txt");

        byte[] filesToByte;
        try {
            filesToByte = ByteStreams.toByteArray(inputStream);
            OutputStream out = igfs.create(file, true);

            out.write(filesToByte);
            out.close();

        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }

        LOGGER.info("Created file path:" + file.toString());

    }
}
