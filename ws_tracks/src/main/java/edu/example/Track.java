/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: Track class (defines a track based on artist, song, and lyrics)
 * Your name(s):
 */

package edu.example;

public class Track {

    private String artist;
    private String song;
    private String lyrics;

    public Track(String artist, String song) {
        this.artist = artist;
        this.song = song;
        lyrics = "";
    }

    public Track(String artist, String song, String lyrics) {
        this.artist = artist;
        this.song = song;
        this.lyrics = lyrics;
    }

    public String getArtist() {
        return artist;
    }

    public String getSong() {
        return song;
    }

    public String getLyrics() {
        return lyrics;
    }

    boolean lyricsHasWord(String word) {
        return lyrics.toLowerCase().contains(word.toLowerCase());
    }
}
