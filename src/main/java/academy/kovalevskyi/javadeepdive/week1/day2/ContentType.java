package academy.kovalevskyi.javadeepdive.week1.day2;

public enum ContentType {

  TEXT_HTML("text/html"),
  APPLICATION_JSON("application/json");

  public final String type;

  ContentType(String type) {
    this.type = type;
  }
}
