package academy.kovalevskyi.javadeepdive.week1.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import academy.kovalevskyi.javadeepdive.week0.day3.RequestException;
import academy.kovalevskyi.javadeepdive.week0.day3.SelectRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.ContentType;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpResponse;

public class CsvColGetHandler implements HttpRequestsHandler {


  private final CSV csv;
  private final String colName;
  private final String path;

  public CsvColGetHandler(CSV csv, String colName, String path) {
    this.csv = csv;
    this.colName = colName;
    this.path = path;
  }

  @Override
  public String path() {
    return path;
  }

  @Override
  public HttpMethod method() {
    return HttpMethod.GET;
  }

  @Override
  public HttpResponse process(HttpRequest request) {
    try {
      var result = new SelectRequest.Builder()
          .from(csv)
          .select(new String[]{colName})
          .build()
          .execute();
      return new HttpResponse.Builder()
          .contentType(ContentType.APPLICATION_JSON)
          .body(prepareJson(result))
          .build();
    } catch (RequestException e) {
      e.printStackTrace();
    }
    return HttpResponse.ERROR_500;
  }

  private String prepareJson(final String[][] array) {
    final var result = new StringBuilder();
    result.append('[');
    for (var element : array) {
      result.append(String.format("\"%s\",", element[0]));
    }
    result.setCharAt(result.length() - 1, ']');
    return result.toString();
  }
}