package academy.kovalevskyi.javadeepdive.week2.day2;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week2.day1.Controller;
import academy.kovalevskyi.javadeepdive.week2.day1.Get;
import academy.kovalevskyi.javadeepdive.week2.day1.Path;
import academy.kovalevskyi.javadeepdive.week2.day1.Post;

@Controller
public class Users {

  private static final UserDB db = new UserDB(new CSV.Builder()
      .header(new String[]{"mail", "firstName", "lastName", "password"})
      .values(new String[0][])
      .build());

  @Get
  @Path("/users")
  public String[] users() {
    try {
      return db.getUsersMails();
    } catch (RequestException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Get
  @Path("/first")
  public User firstUser() {
    try {
      return db.getUser(db.getUsersMails()[0]).get();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Post
  @Path("/users")
  public void addUser(User user) {
    try {
      db.addUser(user);
    } catch (RequestException e) {
      e.printStackTrace();
    }
  }
}
