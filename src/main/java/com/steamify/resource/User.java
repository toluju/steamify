package com.steamify.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
  private String facebookId;
  private String steamId;
  private Date created;
  private List<Game> games = new ArrayList<Game>();

  public String getFacebookId() {
    return facebookId;
  }

  public void setFacebookId(String facebookId) {
    this.facebookId = facebookId;
  }

  public String getSteamId() {
    return steamId;
  }

  public void setSteamId(String steamId) {
    this.steamId = steamId;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public List<Game> getGames() {
    return games;
  }
}