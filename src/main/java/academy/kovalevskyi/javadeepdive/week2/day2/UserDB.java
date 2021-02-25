package academy.kovalevskyi.javadeepdive.week2.day2;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.InsertRequest;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week0.day3.SelectRequest;
import academy.kovalevskyi.javadeepdive.week0.day3.Selector;
import java.util.Arrays;
import java.util.Optional;

public class UserDB {

  private CSV csv;

  public UserDB(CSV csv) {
    this.csv = csv;
  }

  public synchronized void addUser(User user) throws RequestException {
    csv = new InsertRequest.Builder().to(csv).insert(user.toArray()).build().execute();
  }

  public String[] getUsersMails() throws RequestException {
    var result = new SelectRequest.Builder()
        .select(new String[]{"mail"})
        .from(csv)
        .build()
        .execute();
    return Arrays.stream(result).map(entry -> entry[0]).toArray(String[]::new);
  }

  public Optional<User> getUser(String mail) throws RequestException {
    var result = new SelectRequest.Builder()
        .from(csv)
        .where(new Selector.Builder().fieldName("mail").value(mail).build())
        .build().execute();
    if (result.length == 0) {
      return Optional.empty();
    }
    var user = new User();
    user.mail = result[0][0];
    user.firstName = result[0][1];
    user.lastName = result[0][2];
    user.password = result[0][3];
    return Optional.of(user);
  }
}
