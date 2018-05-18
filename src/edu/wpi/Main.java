package edu.wpi;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Main {
    static String rootFolder = System.getProperty("user.home") + "/Desktop/Very Important Work FIles/AlcoGaitDataGathererFiles/alcogait_formatfixV2";
    static String[] cases = {"ACCELEROMETER DATA", "GYROSCOPE DATA", "COMPASS (PHONE)", "HEART RATE DATA"};

    public static void main(String[] args) {

        File root = new File(rootFolder);

        if(root.isDirectory()){
            search(new File[]{root});
        }

    }

    public static void search(File[] files){
        LinkedList<File> waitList = new LinkedList<>();
        waitList.clear();
        String[] BAC_ARRAY = new String[2];
        for(File file: files){
            if(file.isDirectory()){
                search(file.listFiles());
            }else{
                if(file.getName().endsWith(".csv")){
                    if(file.getName().endsWith("phone.csv")) {
                        BAC_ARRAY = split(file, BAC_ARRAY);
                    }else if(file.getName().endsWith("watch.csv")){
                        waitList.add(file);
                    }
                }
            }
        }
        for(File file: waitList){
            split(file, BAC_ARRAY);
        }
    }

    public static String[] split(File inputFile, String[] ARRAY){
        String newTestSubjectFolder = inputFile.getParentFile().getAbsolutePath() + File.separator + inputFile.getName().substring(0,inputFile.getName().length()-4);
        String[] BAC_ARRAY = ARRAY;
        BAC_ARRAY[0] = "BAC: ";
        try {
            CSVReader reader = new CSVReader(new FileReader(inputFile));
            String[] nextLine;
            String walkTypeFolderName = "";
            String fileName;
            File file = null;
            CSVWriter writer = null;
            FileWriter mFileWriter;
            File walkTypeRoot;
            boolean startWriting = false;
            boolean hasWrittenBefore = false;
            while ((nextLine = reader.readNext()) != null) {
                for (WalkType walkType : WalkType.values()) {
                    if (nextLine[0].equals(walkType.toString())) {
                        walkTypeFolderName = newTestSubjectFolder + File.separator + walkType.toNoSpaceString();
                        walkTypeRoot = new File(walkTypeFolderName);
                        walkTypeRoot.mkdirs();
                        startWriting = false;
                        break;
                    }
                }
                if(nextLine[0].startsWith("BAC = ")){
                    BAC_ARRAY[1] = nextLine[0].substring(6,nextLine[0].length());
                }
                int k;
                for(k = 0; k < cases.length;k++){
                    if(nextLine[0].contains(cases[k])){
                        break;
                    }
                }
                switch (k) {
                    case 0:
                        if (hasWrittenBefore) {
                            writer.close();
                        }
                        fileName = walkTypeFolderName + File.separator + "accelerometer.csv";
                        file = new File(fileName);
                        if (file.exists() && !file.isDirectory()) {
                            mFileWriter = new FileWriter(fileName, false);
                        } else {
                            mFileWriter = new FileWriter(fileName);
                        }
                        writer = new CSVWriter(mFileWriter);
                        startWriting = true;
                        writer.writeNext(BAC_ARRAY);
                        break;
                    case 1:
                        if (hasWrittenBefore) {
                            writer.close();
                        }
                        fileName = walkTypeFolderName + File.separator + "gyroscope.csv";
                        file = new File(fileName);
                        if (file.exists() && !file.isDirectory()) {
                            mFileWriter = new FileWriter(fileName, false);
                        } else {
                            mFileWriter = new FileWriter(fileName);
                        }
                        writer = new CSVWriter(mFileWriter);
                        startWriting = true;
                        writer.writeNext(BAC_ARRAY);
                        break;
                    case 2:
                        if (hasWrittenBefore) {
                            writer.close();
                        }
                        fileName = walkTypeFolderName + File.separator + "compass.csv";
                        file = new File(fileName);
                        if (file.exists() && !file.isDirectory()) {
                            mFileWriter = new FileWriter(fileName, false);
                        } else {
                            mFileWriter = new FileWriter(fileName);
                        }
                        writer = new CSVWriter(mFileWriter);
                        startWriting = true;
                        writer.writeNext(BAC_ARRAY);
                        break;
                    case 3:
                        if (hasWrittenBefore) {
                            writer.close();
                        }
                        fileName = walkTypeFolderName + File.separator + "heart_rate.csv";
                        file = new File(fileName);
                        if (file.exists() && !file.isDirectory()) {
                            mFileWriter = new FileWriter(fileName, false);
                        } else {
                            mFileWriter = new FileWriter(fileName);
                        }
                        writer = new CSVWriter(mFileWriter);
                        startWriting = true;
                        writer.writeNext(BAC_ARRAY);
                        break;
                }
                if (startWriting) {
                    hasWrittenBefore = true;
                    writer.writeNext(nextLine);
                }
            }
            if (hasWrittenBefore) {
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("File Extraction Error!");
            e.printStackTrace();
        }
        System.out.println("Complete!");
        return BAC_ARRAY;
    }
}
