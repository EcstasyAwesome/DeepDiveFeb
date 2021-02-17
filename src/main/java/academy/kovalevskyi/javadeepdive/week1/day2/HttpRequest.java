package academy.kovalevskyi.javadeepdive.week1.day2;

import java.util.Optional;

public record HttpRequest(
    String path,
    HttpMethod method,
    Optional<String> body,
    ContentType type,
    HttpVersion version) {

  public static class Builder {

    private String path;
    private String body;
    private HttpMethod method;
    private HttpVersion version;
    private ContentType type;

    public Builder() {
      path = "/";
      method = HttpMethod.GET;
      type = ContentType.TEXT_HTML;
      version = HttpVersion.HTTP_1_1;
    }

    public Builder path(String path) {
      this.path = path;
      return this;
    }

    public Builder method(HttpMethod method) {
      this.method = method;
      return this;
    }

    public Builder body(String body) {
      this.body = body;
      return this;
    }

    public Builder contentType(ContentType type) {
      this.type = type;
      return this;
    }

    public Builder httpVersion(HttpVersion version) {
      this.version = version;
      return this;
    }

    public HttpRequest build() {
      return new HttpRequest(path, method, Optional.ofNullable(body), type, version);
    }
  }
}
