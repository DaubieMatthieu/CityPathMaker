package main.java.helpers;

import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import main.java.gtfs_vo.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public interface gtfsHelper {
    String gtfsDir = "gtfs/";

    //initiate the gtfs data from the given zipFile
    static void initiateDataFrom(String zipFileName) {
        System.out.println("Initiating data");
        System.out.println("    Fetching zipped files from " + zipFileName);
        Map<String, BufferedReader> zippedFiles = null;
        try {
            zippedFiles = getFiles(zipFileName);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        assert zippedFiles != null;
        //order is important because some objects initiate themselves by linking with other objects,
        // which therefore need to be already initiated
        gtfsHelper.initiateVOFrom(zippedFiles.get("stops.txt"), Stop.class);
        gtfsHelper.initiateVOFrom(zippedFiles.get("routes.txt"), Route.class);
        gtfsHelper.initiateVOFrom(zippedFiles.get("trips.txt"), Trip.class);
        gtfsHelper.initiateVOFrom(zippedFiles.get("stop_times.txt"), StopTime.class);
    }

    //can be called with full path or with relative path from resources/gtfs
    //return the content of the zipFile as a HashMap
    static Map<String, BufferedReader> getFiles(String zipFilePath) throws IOException, URISyntaxException {
        File resource;
        resource = new File(zipFilePath);
        if (!resource.exists())
            resource = new File(gtfsHelper.class.getClassLoader().getResource(gtfsDir + zipFilePath).toURI());
        if (!resource.exists()) throw new IllegalArgumentException("file " + zipFilePath + " not found!");
        ZipFile zipFile = new ZipFile(resource);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();

        Map<String, BufferedReader> files = new HashMap<>();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
            files.put(entry.getName(), reader);
        }
        return files;
    }

    //transform every line of the dataFile to an instance of the specified class clazz
    static <T extends GenericGtfsVO> void initiateVOFrom(BufferedReader dataFile, Class<T> clazz) {
        String className = clazz.getSimpleName();
        System.out.println("    Instantiating " + className + "s");
        final HashMap<Class<? extends CsvException>, Integer> exceptions = new HashMap<>();
        List<T> data = new CsvToBeanBuilder<T>(dataFile).withType(clazz).withExceptionHandler(e -> {
            int count = exceptions.getOrDefault(e.getClass(), 0);
            exceptions.put(e.getClass(), count + 1);
            return null;
        }).build().parse();
        for (T vo : data) vo.register();
        for (Class<?> ex : exceptions.keySet())
            System.out.println("        " + exceptions.get(ex) +
                    " " + ex.getSimpleName() + " occurred when instantiating " + className + "s");
        System.out.println("        " + data.size() + " " + className + " where instantiated");
    }

    //scan resources/gtfs directory to select a gtfs data feed to use
    //if only one data feed is found, it will automatically be used
    static void scanResources() {
        try {
            System.out.println("Fetching resources..");
            URL resource = gtfsHelper.class.getClassLoader().getResource(gtfsDir);
            assert resource != null;
            File[] files = new File(resource.toURI()).listFiles();
            assert files != null;
            if (files.length == 1) {
                initiateDataFrom(files[0].getPath());
                return;
            }
            System.out.println("Found " + files.length + " files:");
            for (int i = 0; i < files.length; i++) System.out.println("    " + i + ": " + files[i].getName());
            Scanner scanner = new Scanner(System.in);
            int fileNumber = -1;
            while (0 > fileNumber || fileNumber >= files.length) {
                System.out.print("Enter the number of the file to use: ");
                fileNumber = Integer.parseInt(scanner.next());
            }
            initiateDataFrom(files[fileNumber].getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
