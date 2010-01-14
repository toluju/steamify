package com.steamify.resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
  private String name;
  private Date created;
  private List<Game> games = new ArrayList<Game>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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