package fpts.toolkit;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static fpts.toolkit.Export.decryptString;

/**
 *
 * Created by George Herde on 3/12/16.
 *
 * Collection of functions to parse a CSV file
 */
public class CSVParser {
    /**
     * Parses a csv file
     * @param filepath - the filepath pointing to the file to parse
     * @param encrypted - Boolean
     * @return A 2D array representing lines and items from the csv
     */
    public static List<List<String>> parseFile(String filepath, boolean encrypted) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<List<String>> parsedFile = new ArrayList<>();
        try {
            if (br != null) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (encrypted){
                        parsedFile.add(parseLine(decryptString(line)));
                    } else{
                        parsedFile.add(parseLine(line));
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (br != null) {
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return parsedFile;
    }

    /**
     * Parses a single line in a csv file
     *
     * @param line - the line to be parsed
     * @return - A list of items split on ","
     */
    private static List<String> parseLine(String line) {
        String[] splitLine = line.split("(?!^)");
        List<String> parsedLine = new ArrayList<>();

        int i = 0;
        String field = "";
        while (i < splitLine.length) {
            switch (splitLine[i]) {
                case "\"":
                    i++;
                    while (true) {
                        if (splitLine.length == i){
                            break;
                        } else if (!splitLine[i].equals("\"")) {
                            field += splitLine[i++];
                        } else if (splitLine[i].equals("\"")) {
                                if (splitLine.length == i+1){
                                    parsedLine.add(field);
                                    field = "";
                                    break;
                                }
                                else if (splitLine[i + 1].equals(",")) {
                                    parsedLine.add(field);
                                    field = "";
                                    i += 2;
                                    break;
                                } else {
                                    field += splitLine[i++];
                                }
                            }
                        }
                    break;
                case ",":
                    parsedLine.add(field.trim());
                    field = "";
                    i++;
                    break;
                default:
                    field += splitLine[i++];
                    break;
            }
        }
        return parsedLine;
    }
}
