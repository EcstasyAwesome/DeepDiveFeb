package academy.kovalevskyi.javadeepdive.week1;

import academy.kovalevskyi.javadeepdive.week1.day1.ConcurrentHttpServer;
import java.util.Scanner;

public class HttpServerTest {

  public static void main(String[] args) {
//    var server = new HttpServer();
    var server = new ConcurrentHttpServer();
    var thread = new Thread(server);
    thread.start();
    try (var scanner = new Scanner(System.in)) {
      while (scanner.hasNext()) {
        var text = scanner.next();
        if (text.equalsIgnoreCase("stop")) {
          server.stop();
          break;
        }
      }
    }
  }
}
