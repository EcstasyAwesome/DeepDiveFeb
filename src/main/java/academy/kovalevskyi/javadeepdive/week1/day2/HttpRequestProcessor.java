package academy.kovalevskyi.javadeepdive.week1.day2;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.StringJoiner;

public class HttpRequestProcessor implements Runnable, Closeable {

  private final Socket socket;
  private final List<HttpRequestsHandler> handlers;

  public HttpRequestProcessor(Socket socket, List<HttpRequestsHandler> handlers) {
    this.socket = socket;
    this.handlers = handlers;
  }

  public void execute() throws IOException {
    final var request = parse();
    for (var handler : handlers) {
      if (handler.path().equals(request.path()) && handler.method() == request.method()) {
        send(handler.process(request));
        return;
      }
    }
    send(HttpResponse.ERROR_404);
  }

  @Override
  public void run() {
    try (this) {
      execute();
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void close() throws IOException {
    if (!socket.isInputShutdown()) {
      socket.shutdownInput();
    }
    if (!socket.isOutputShutdown()) {
      socket.shutdownOutput();
    }
    if (!socket.isClosed()) {
      socket.close();
    }
  }

  private HttpRequest parse() throws IOException {
    final var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    final var joiner = new StringJoiner("\n");
    var headerLine = (String) null;
    while (!(headerLine = reader.readLine()).isEmpty()) {
      joiner.add(headerLine);
    }
    final var headers = joiner.toString().split("\n");
    final var firstLine = headers[0].split(" ");
    final var body = new StringBuilder();
    while (reader.ready()) {
      body.append((char) reader.read());
    }
    return new HttpRequest.Builder()
        .method(HttpMethod.valueOf(firstLine[0]))
        .path(firstLine[1])
        .body(body.toString())
        .httpVersion(HttpVersion.HTTP_1_1)
        .build();
  }

  private void send(final HttpResponse response) throws IOException {
    final var output = socket.getOutputStream();
    output.write(String.format(
        "%s %d %s\r\n",
        response.version().version,
        response.status().code,
        response.status().message).getBytes());
    var length = 0;
    if (!response.body().isEmpty()) {
      length = response.body().length() + 4;
    }
    output.write(String.format("Content-Length: %d\r\n", length).getBytes());
    output.write(String.format("Content-Type: %s\r\n\r\n", response.type().type).getBytes());
    output.write(String.format("%s\r\n\r\n", response.body()).getBytes());
  }
}
