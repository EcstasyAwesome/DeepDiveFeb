package academy.kovalevskyi.javadeepdive.week1.day0;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class HttpRequestsHandler {

  private final Socket socket;

  public HttpRequestsHandler(Socket socket) {
    this.socket = socket;
  }

  public void executeRequest() throws IOException {
    try (var reader = new StdBufferedReader(new InputStreamReader(socket.getInputStream()));
        var outputStream = socket.getOutputStream()) {
      while (reader.hasNext()) {
        System.out.println(reader.line());
      }
      write(outputStream);
    }
  }

  private void write(OutputStream output) throws IOException {
    output.write("HTTP/1.1 200 OK\r\n".getBytes());
    output.write("Content-Length: 20\r\n".getBytes());
    output.write("Content-Type: text/html\r\n\r\n".getBytes());
    output.write("<b>It works!</b>\r\n\r\n".getBytes());
  }
}
