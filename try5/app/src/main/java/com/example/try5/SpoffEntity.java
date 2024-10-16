package com.example.try5;

import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Seed;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
public class SpoffEntity extends SpotifyAbs {
    public SpoffEntity(String tokens){
        token = tokens;

        this.api = new SpotifyApi();

        api.setAccessToken(token);

        this.spotify = api.getService();
        this.inIDs = new ArrayList<String>();
        this.finalList = new ArrayList<String>();
    }
    public float getFeatures(String id, int i){
        ret = 0;
        Log.d("Getting features", "Getting Features");
        switch(i){
            case(0):
                ret = spotify.getTrackAudioFeatures(id).danceability;
                break;
            case(1):
                ret = spotify.getTrackAudioFeatures(id).instrumentalness;
                break;
            case(2):
                ret = spotify.getTrackAudioFeatures(id).tempo;
                break;
            case(3):
                ret = spotify.getTrackAudioFeatures(id).key;
                break;
            case(4):
                ret = spotify.getTrackAudioFeatures(id).valence;
                break;
            case(5):
                ret = spotify.getTrackAudioFeatures(id).duration_ms;
                Log.d("duration", "+ " + spotify.getTrack(id).name);

                break;
        }
        return ret;
    }

    public ArrayList<String> search(ArrayList<Button> list){
        currentList = new ArrayList<String>();
        try {
            try {
                Pager<Track> result = spotify.getTopTracks();
                for (int i = 0; i < list.size(); i++) {
                    String text = result.items.get(i).name + " - " + result.items.get(i).artists.get(0).name;
                    list.get(i).setText(text);
                    currentList.add(result.items.get(i).id);
                }
            } catch(IndexOutOfBoundsException e){
                currentList = search("a", list);
            }
        } catch (RetrofitError error) {}
        return currentList;
    }

    public ArrayList<String> search(String in, ArrayList<Button> list){
        currentList = new ArrayList<String>();
        spotify.searchTracks(in, new Callback<TracksPager>() {
            @Override
            public void success(TracksPager result, Response response) {
                if(result.tracks.items.size() > 0) {
                    for (int i = 0; i < list.size() ; i++) {
                        String text = result.tracks.items.get(i).name + " - " + result.tracks.items.get(i).artists.get(0).name;
                        list.get(i).setText(text);
                        currentList.add(result.tracks.items.get(i).id);
                    }
                }
            }
            @Override
            public void failure(RetrofitError err) {}
        });
        return currentList;
    }

    public void addSong(String id){
        inIDs.add(id);
    }
    public void removeSong(String id){
        inIDs.remove(id);
    }

    public ArrayList<String> getInIds(){
        return inIDs;
    }
    public ArrayList<String> getReccomendation(ArrayList<String> in, int resultSize, float dance, float inst, float vale, int dur, float temp, int avKey){
        key = new HashMap<String, Object>();
        test = new Seed[in.size()];
        for(int i = 0; i < in.size(); i++){
            test[i] = new Seed();
            test[i].id = in.get(i);
            key.put("seed_tracks", in.get(i));
        }
        key.put("limit", resultSize);
        if(dance != -5) {
            key.put("min_danceability", dance - 0.5);
            key.put("max_danceability", dance + 0.5);
        }
        if(dur != -5) {
            key.put("min_duration_ms", dur - 30000);
            key.put("max_duration_ms", dur + 30000);
        }
        if(inst != -5) {
            key.put("min_instrumentalness", inst - 0.5);
            key.put("max_instrumentalness", inst + 0.5);
        }
        if(avKey != -5) {
            key.put("min_key", avKey - 1);
            key.put("max_key", avKey + 1);
        }
        if(temp != -5) {
            key.put("min_tempo", temp - 10);
            key.put("max_tempo", temp + 10);
        }
        if(vale != -5) {
            key.put("min_valence", vale - 0.5);
            key.put("max_valence", vale + 0.5);
        }


        result = spotify.getRecommendations(key);
        int y = 0;
        try {
            while (y < resultSize) {
                finalList.add(result.tracks.get(y).id);
                y++;
            }
        } catch(IndexOutOfBoundsException e){
            //out of recommendaions
        }
        return finalList;
    }

    public void addPlaylist(ArrayList<String> ids){
        userId = spotify.getMe().id;
        HashMap key = new HashMap<String, Object>();
        key.put("name", "Simple Recommendations");
        key.put("description", "Your playlist generated by Simple Recommend");
        key.put("public", false);
        add = spotify.createPlaylist(userId, key);
        HashMap key2 = new HashMap<String, Object>();
        HashMap key3 = new HashMap<String, Object>();
        for(int i=0; i < ids.size(); i++){
            key2.put("uris", spotify.getTrack(ids.get(i)).uri);
            key3.put("position", i);
            spotify.addTracksToPlaylist(userId, add.id, key2, key3);
        }
    }

    public String getList(ArrayList<String> ids, int size){
        txt = "";
        for(int i=0; i < ids.size(); i++) {
            rn = spotify.getTrack(ids.get(i));
            txt = txt + ((i + 1) + ". " + rn.name + " - " + rn.artists.get(0).name + "\n");
        }
        if(ids.size() < size)
            txt += "Spotify ran out of songs to recommend :( \n try selecting new filters";
        return txt;
    }
}
