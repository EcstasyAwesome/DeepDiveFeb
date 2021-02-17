package academy.kovalevskyi.javadeepdive.week1.day2;

public record HttpResponse(
    ResponseStatus status,
    ContentType type,
    String body,
    HttpVersion version) {

  public final static HttpResponse ERROR_404;
  public final static HttpResponse OK_200;
  public final static HttpResponse ERROR_500;

  static {
    ERROR_404 = new Builder().status(ResponseStatus.ERROR_404).build();
    OK_200 = new Builder().status(ResponseStatus.OK).build();
    ERROR_500 = new Builder().status(ResponseStatus.ERROR_500).build();
  }

  public static class Builder {

    private ResponseStatus status;
    private ContentType type;
    private String body;
    private HttpVersion version;

    public Builder() {
      version = HttpVersion.HTTP_1_1;
      status = ResponseStatus.OK;
      type = ContentType.TEXT_HTML;
      body = "";
    }

    public Builder status(ResponseStatus status) {
      this.status = status;
      return this;
    }

    public Builder contentType(ContentType type) {
      this.type = type;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder httpVersion(HttpVersion version) {
      this.version = version;
      return this;
    }

    public HttpResponse build() {
      return new HttpResponse(status, type, body, version);
    }
  }

  public enum ResponseStatus {
    OK(200, "OK"),
    ERROR_404(404, "not found"),
    ERROR_500(500, "server error");

    public final int code;
    public final String message;

    ResponseStatus(int code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
