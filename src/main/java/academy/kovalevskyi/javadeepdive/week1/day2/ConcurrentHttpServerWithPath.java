package academy.kovalevskyi.javadeepdive.week1.day2;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHttpServerWithPath extends Thread {

  private final ExecutorService executor;
  private final List<HttpRequestsHandler> handlers;
  private volatile boolean live;

  public ConcurrentHttpServerWithPath() {
    live = true;
    executor = Executors.newCachedThreadPool();
    handlers = new CopyOnWriteArrayList<>();
  }

  public void addHandler(HttpRequestsHandler handler) {
    handlers.add(handler);
  }

  public static void main(String[] args) {
    var serverThread = new ConcurrentHttpServerWithPath();
    serverThread.addHandler(new HttpRequestsHandler() {
      @Override
      public String path() {
        return "/hi";
      }

      @Override
      public HttpMethod method() {
        return HttpMethod.GET;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        return new HttpResponse.Builder().body("<h1>HI!</h1>").build();
      }
    });
    serverThread.start();
  }

  public void run() {
    try (var serverSocket = new ServerSocket(8080)) {
      while (live) {
        executor.submit(new HttpRequestProcessor(serverSocket.accept(), handlers));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void stopServer() {
    live = false;
  }

  public boolean isLive() {
    return live;
  }
}
