/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructor: Thyago Mota
 * Description: Hwk 04 - StarWarsListing
 * Your name(s):
 */

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class StarWarsListing {

    static final String API_URL = "https://swapi.co/api/people/?format=json";
    static final int RESPONSE_CODE_OK = 200;

    public static void main(String[] args) throws IOException {

        // TODO: connect to API_URL
        URL url = new URL(API_URL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

        // TODO: send a GET request; quit if response code is different than 200
        int responseCode = httpConn.getResponseCode();
        if (responseCode != RESPONSE_CODE_OK) {
            System.out.println("Ops, something is not right!");
            System.exit(1);
        }

        // TODO: save the response into a text string
        String rawJson = "";
        Scanner sc = new Scanner(httpConn.getInputStream());
        while (sc.hasNext()) {
            String line = sc.nextLine();
            rawJson += line;
        }
        sc.close();
//        System.out.println(rawJson);

        // TODO: process the response, printing the name of the Star Wars character, birth year, and gender
        Gson gson = new Gson();
        Result result = gson.fromJson(rawJson, Result.class);
//        System.out.println(result);
        for (int i = 0; i < result.results.length; i++) {
            People people = result.results[i];
            System.out.println(people.name + ", " + people.birth_year + ", " + people.gender);
        }

        // TODO: optional challenge: get all characters, not only the ones returned by the first page

    }
}