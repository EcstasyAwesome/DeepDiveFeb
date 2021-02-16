package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  private volatile boolean live = true;

  public static void main(String[] args) {
    var server = new HttpServer();
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

  @Override
  public void run() {
    try (var serverSocket = new ServerSocket(8080)) {
      while (live) {
        try (var socket = serverSocket.accept()) {
          new HttpRequestsHandler(socket).executeRequest();
        }
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void stop() {
    live = false;
  }

  public boolean isLive() {
    return live;
  }
}
