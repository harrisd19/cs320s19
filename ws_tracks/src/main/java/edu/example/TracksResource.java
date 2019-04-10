/*
 * CSCI320 - Networking & Distributed Computing - Spring 2019
 * Instructors: Thyago Mota
 * Description: TrackResource class (defines a web service resource for querying tracks)
 * Your name(s):
 */

package edu.example;

import com.google.gson.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;


@Path("tracks")
public class TracksResource {

    // TODO: return a JSON string with the results based on the query parameters;
    //  if artist parameter is not null, search by artist;
    //  if song parameter is not null, search by song title;
    //  if word parameter is not null, search by word in lyrics;
    //  finally, if all parameters are null, return ALL tracks
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTracks(
            @QueryParam("artist") String artist,
            @QueryParam("song") String song,
            @QueryParam("word") String word
            ) {

        TracksDataStore ds = TracksDataStore.getInstance();
        GsonBuilder gsonBuilder = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting();
//        GsonBuilder gsonBuilder = new GsonBuilder();

        Gson gson = gsonBuilder.create();

        if (artist != null)
            return gson.toJson(ds.getTracksByArtist(artist));

        if (song != null)
            return gson.toJson(ds.getTracksBySong(song));

        if (word != null)
            return gson.toJson(ds.getTracksByWord(word));

        return gson.toJson(ds.getTracks());
    }
}
