package academy.kovalevskyi.javadeepdive.week1.day0;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestsHandler implements Runnable {

  protected final Socket socket;

  public HttpRequestsHandler(Socket socket) {
    this.socket = socket;
  }

  public void executeRequest() {
    try (var inputStream = socket.getInputStream();
        var outputStream = socket.getOutputStream()) {
      read(inputStream);
      write(outputStream);
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  @Override
  public void run() {
    executeRequest();
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void read(final InputStream inputStream) throws IOException {
    var reader = new StdBufferedReader(new InputStreamReader(inputStream));
    var result = new StringBuilder();
    String line;
    while (!(line = reader.line()).isBlank()) {
      result.append(line).append("\r\n");
    }
    System.out.println(result);
  }

  private void write(final OutputStream output) throws IOException {
    output.write("HTTP/1.1 200 OK\r\n".getBytes());
    output.write("Content-Length: 20\r\n".getBytes());
    output.write("Content-Type: text/html\r\n\r\n".getBytes());
    output.write("<b>It works!</b>\r\n\r\n".getBytes());
  }
}
