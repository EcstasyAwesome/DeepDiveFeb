package academy.kovalevskyi.javadeepdive.week2.day2;

public class User {

  public String mail;
  public String firstName;
  public String lastName;
  public String password;

  public String[] toArray() {
    return new String[]{mail, firstName, lastName, password};
  }
}
