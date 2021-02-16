package academy.kovalevskyi.javadeepdive.week1.day0;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class HttpServer implements Runnable {

  protected volatile boolean live = true;

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
