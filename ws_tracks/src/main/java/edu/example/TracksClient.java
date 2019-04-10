/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: TracksClient class (implements a simple client that searches songs for a given artist)
 * Your name(s):
 */

package edu.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class TracksClient {

    static final String TRACKS_URL = "http://localhost:8080/tracks";

    // TODO: ask for the name of an artist and then return all songs for that artist using the web service
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        System.out.println("Artist? ");
        String artist = sc.nextLine();
        artist = artist.replaceAll(" ", "%20");

        // make the connection
        URL url = new URL(TRACKS_URL + "?artist=" + artist);
        System.out.println(url);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        // send the request
        int responseCode = httpConn.getResponseCode();

        // checking response code
        if (responseCode != 200) {
            System.out.println("Something is not quite right...");
            System.out.println("Response code was " + responseCode);
            System.exit(1);
        }

        // read the response
        sc = new Scanner(httpConn.getInputStream());
        String rawJson = "";
        while (sc.hasNext()) {
            String line = sc.nextLine();
            rawJson += line;
        }
        sc.close();

        //System.out.println(rawJson);

        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        Track tracks[] = gson.fromJson(rawJson, Track[].class);
        for (int i = 0; i < tracks.length; i++) {
            Track track = tracks[i];
            rawJson = gson.toJson(track);
            System.out.println(rawJson);
        }
    }
}
