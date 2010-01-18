package com.steamify.resource;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.sun.jersey.api.view.Viewable;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

@Path("/")
public class IndexResource {
  public static final DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
  public static final SAXBuilder xmlBuilder = new SAXBuilder();
  public static final FetchOptions limit10 = FetchOptions.Builder.withLimit(10);

  @GET
  public Viewable index(@QueryParam("fb_sig_canvas_user") String facebookId) {
    try {
      Entity userEntity = dataStore.get(KeyFactory.createKey("User", facebookId));
      return viewUser(userEntity);
    }
    catch (EntityNotFoundException e) {
      Map model = new HashMap();
      model.put("facebookId", facebookId);
      return new Viewable("/newuser", model);
    }
  }
  
  @GET @Path("/profiletab")
  public Viewable profileTab(@QueryParam("fb_sig_profile_user") String facebookId) throws EntityNotFoundException {
    Entity userEntity = dataStore.get(KeyFactory.createKey("User", facebookId));
    return viewUser(userEntity);
  }

  private Viewable viewUser(Entity userEntity) {
    User user = new User();
    user.setFacebookId((String) userEntity.getProperty("facebookId"));
    user.setSteamId((String) userEntity.getProperty("steamId"));
    user.setCreated((Date) userEntity.getProperty("created"));

    for (Entity gameEntity : dataStore.prepare(new Query(userEntity.getKey())).asList(limit10)) {
      Game game = new Game();
      game.setName((String) gameEntity.getProperty("name"));
      game.setUrl((String) gameEntity.getProperty("url"));
      user.getGames().add(game);
    }

    Map model = new HashMap();
    model.put("user", user);
    return new Viewable("/user", model);
  }

  @POST
  public Viewable newUser(@FormParam("facebookId") String facebookId, 
                          @FormParam("steamId") String steamId) throws JDOMException, IOException {
    Document doc = xmlBuilder.build(new URL("http://steamcommunity.com/id/" + steamId + "/games?xml=1"));

    Date now = new Date();
    Entity userEntity = new Entity("User", facebookId);
    userEntity.setProperty("facebookId", facebookId);
    userEntity.setProperty("steamId", steamId);
    userEntity.setProperty("created", now);
    Key userKey = dataStore.put(userEntity);

    User user = new User();
    user.setCreated(now);
    user.setFacebookId(facebookId);
    user.setSteamId(steamId);

    for (Object elementObj : doc.getRootElement().getChild("games").getChildren()) {
      Element element = (Element) elementObj;
      String gameName = element.getChildTextTrim("name");
      String gameUrl = element.getChildTextTrim("storeLink");
      Key gameKey = userKey.getChild("Game", gameName);
      Entity gameEntity = new Entity(gameKey);
      gameEntity.setProperty("name", gameName);
      gameEntity.setProperty("url", gameUrl);
      dataStore.put(gameEntity);

      Game game = new Game();
      game.setName(gameName);
      game.setUrl(gameUrl);
      user.getGames().add(game);
    }

    Map model = new HashMap();
    model.put("user", user);
    return new Viewable("/user", model);
  }
}