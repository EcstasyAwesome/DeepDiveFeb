package academy.kovalevskyi.javadeepdive.week1.day1;

import academy.kovalevskyi.javadeepdive.week1.day0.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day0.HttpServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHttpServer extends HttpServer implements Runnable {

  private final ExecutorService executorService;

  public ConcurrentHttpServer() {
    executorService = Executors.newCachedThreadPool();
  }

  @Override
  public void run() {
    try (var serverSocket = new ServerSocket(8080)) {
      while (live) {
        try (var socket = serverSocket.accept()) {
          executorService.submit(new HttpRequestsHandler(socket));
        }
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }
}
