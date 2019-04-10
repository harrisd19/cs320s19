/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: TrackDataStore class (defines a data store for tracks)
 * Your name(s):
 */

package edu.example;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class TracksDataStore {

    private static final String DATA_FILE_NAME = "tracks.json";
    private static TracksDataStore instance = null;
    private List<Track> tracks;

    // TODO: read all tracks in DATA_FILE_NAME, adding the track objects into the tracks list
    private TracksDataStore() {
        tracks = new LinkedList<>();
        try {
            Gson gson = new Gson();
            Scanner sc = new Scanner(new FileInputStream("resources/" + DATA_FILE_NAME));
            while (sc.hasNextLine()) {
                String rawJson = sc.nextLine();
                Track track = gson.fromJson(rawJson, Track.class);
                tracks.add(track);
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static TracksDataStore getInstance() {
        if (instance == null)
            instance = new TracksDataStore();
        return instance;
    }

    // TODO: return all tracks
    public Collection<Track> getTracks() {
        return tracks;
    }

    // TODO: return all tracks from the given artist
    public Collection<Track> getTracksByArtist(final String artist) {
        List<Track> tracksByArtist = new LinkedList<>();
        for (Track track: tracks)
            if (track.getArtist().equalsIgnoreCase(artist))
                tracksByArtist.add(track);
        return tracksByArtist;
    }

    // TODO: return all tracks with the same song title
    public Collection<Track> getTracksBySong(final String song) {
        List<Track> tracksBySong = new LinkedList<>();
        for (Track track: tracks)
            if (track.getSong().equalsIgnoreCase(song))
                tracksBySong.add(track);
        return tracksBySong;
    }

    // TODO: return all tracks that has lyrics with the given word
    public Collection<Track> getTracksByWord(final String word) {
        List<Track> tracksByWord = new LinkedList<>();
        for (Track track: tracks)
            if (track.getLyrics().toLowerCase().contains(word.toLowerCase()))
                tracksByWord.add(track);
        return tracksByWord;
    }
}
