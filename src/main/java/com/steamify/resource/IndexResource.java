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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
  public Viewable index() {
    Map model = new HashMap();

    List<User> users = new ArrayList<User>();

    for (Entity entity : dataStore.prepare(new Query("User")).asList(limit10)) {
      User user = new User();
      user.setName((String) entity.getProperty("name"));
      user.setCreated((Date) entity.getProperty("created"));
      users.add(user);
    }

    model.put("users", users);
    return new Viewable("/index", model);
  }

  @POST @Path("/user")
  public Viewable newUser(@FormParam("name") String name) throws JDOMException, IOException {
    Entity user = new Entity("User", name);
    user.setProperty("name", name);
    user.setProperty("created", new Date());
    Key userKey = dataStore.put(user);

    Document doc = xmlBuilder.build(new URL("http://steamcommunity.com/id/" + name + "/games?xml=1"));
    for (Object elementObj : doc.getRootElement().getChild("games").getChildren()) {
      Element element = (Element) elementObj;
      String gameName = element.getChildTextTrim("name");
      String gameUrl = element.getChildTextTrim("storeLink");
      Key gameKey = userKey.getChild("Game", gameName);
      Entity game = new Entity(gameKey);
      game.setProperty("name", gameName);
      game.setProperty("url", gameUrl);
      dataStore.put(game);
    }

    return index();
  }

  @GET @Path("/user/{name}")
  public Viewable getUser(@PathParam("name") String name) throws EntityNotFoundException {
    Map model = new HashMap();
    Entity userEntity = dataStore.get(KeyFactory.createKey("User", name));
    User user = new User();
    user.setName((String) userEntity.getProperty("name"));
    user.setCreated((Date) userEntity.getProperty("created"));

    for (Entity gameEntity : dataStore.prepare(new Query("Game")).asList(limit10)) {
      Game game = new Game();
      game.setName((String) gameEntity.getProperty("name"));
      game.setUrl((String) gameEntity.getProperty("url"));
      user.getGames().add(game);
    }

    model.put("user", user);
    return new Viewable("/user", model);
  }
}