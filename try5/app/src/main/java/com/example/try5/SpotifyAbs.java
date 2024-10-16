package com.example.try5;

import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Recommendations;
import kaaes.spotify.webapi.android.models.Seed;
import kaaes.spotify.webapi.android.models.Track;

abstract class SpotifyAbs {
    String token;
    SpotifyService spotify;
    SpotifyApi api;
    ArrayList<String> inIDs;
    ArrayList<String> currentList;
    ArrayList<String> finalList;
    float ret = 0;
    Seed[] test;
    HashMap<String, Object> key;
    Recommendations result;
    String userId;
    Playlist add;
    Track rn;
    String txt;

    public abstract ArrayList<String> search(ArrayList<Button> list);
    public abstract ArrayList<String> search(String in, ArrayList<Button> list);
    public abstract void addSong(String id);
    public abstract void removeSong(String id);
    public abstract float getFeatures(String id, int i);
    public abstract String getList(ArrayList<String> ids, int size);
    public abstract ArrayList<String> getInIds();
    public abstract ArrayList<String> getReccomendation(ArrayList<String> in, int resultSize, float dance, float inst, float vale, int dur, float temp, int avKey);
    public abstract void addPlaylist(ArrayList<String> ids);
}
