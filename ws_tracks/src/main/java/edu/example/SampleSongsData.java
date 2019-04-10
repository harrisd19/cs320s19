package edu.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;

public class SampleSongsData {

    // original dataset: https://www.kaggle.com/mousehead/songlyrics#songdata.csv
    public static void main(String[] args) throws FileNotFoundException {

        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping();
        Gson gson = gsonBuilder.create();
        Random rnd = new Random();
        Scanner sc = new Scanner(new FileInputStream("resources/songdata.csv"));
        PrintStream out = new PrintStream("resources/tracks.json");
        sc.nextLine(); // ignore header line
        String outLine = "";
        int count = 0;
        while (sc.hasNextLine()) {
            String inLine = sc.nextLine();
            outLine += inLine;
            if (inLine.equals("\"")) {
                // remove space duplicates
                outLine = outLine.replaceAll("\\s+", " ");
//                System.out.println(outLine);
                int i = 0;
                String artist = "";
                boolean inQuote = false;
                while (true) {
                    char c = outLine.charAt(i++);
                    if (c == '"') {
                        inQuote = !inQuote;
                        continue;
                    }
                    if (c == ',' && !inQuote)
                        break;
                    artist += c;
                }
                String song = "";
                while (true) {
                    char c = outLine.charAt(i++);
                    if (c == '"') {
                        inQuote = !inQuote;
                        continue;
                    }
                    if (c == ',' && !inQuote)
                        break;
                    song += c;
                }
                String url = "";
                while (true) {
                    char c = outLine.charAt(i++);
                    if (c == '"') {
                        inQuote = !inQuote;
                        continue;
                    }
                    if (c == ',' && !inQuote)
                        break;
                    url += c;
                }
                String lyrics = outLine.substring(i).replaceAll("\"", "");
//                System.out.println(artist);
//                System.out.println(song);
//                System.out.println(lyrics);
                Track track = new Track(artist, song, lyrics);
                String rawJson = gson.toJson(track);
//                System.out.println(rawJson);
//                count++;
//                if (count == 500)
//                    break;
                outLine = "";
                if (rnd.nextInt(100) == 0)
                    out.println(rawJson);
            }
        }
        sc.close();
        out.close();
    }
}
