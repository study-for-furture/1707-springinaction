package com.chap04.concert;


import java.util.List;

public class BlankDisc {

  private String title;
  private String artist;
  private List<String> tracks;

  public BlankDisc() {

  }

  public BlankDisc(String title, String artist) {
    this.title = title;
    this.artist = artist;
  }
  
  public String getTitle() {
    return title;
  }
  
  public String getArtist() {
    return artist;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public List<String> getTracks() {
    return tracks;
  }

  public void setTracks(List<String> tracks) {
    this.tracks = tracks;
  }

  public void playTrack(int trackNumber){

  }
}
