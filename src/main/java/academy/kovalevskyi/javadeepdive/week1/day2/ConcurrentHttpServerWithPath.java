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
    this.live = true;
    this.executor = Executors.newCachedThreadPool();
    this.handlers = new CopyOnWriteArrayList<>();
  }

  public void addHandler(HttpRequestsHandler handler) {
    this.handlers.add(handler);
  }

  public void addHandlers(List<HttpRequestsHandler> handlers) {
    this.handlers.addAll(handlers);
  }

  public void run() {
    try (var serverSocket = new ServerSocket(8080)) {
      while (live) {
        this.executor.submit(new HttpRequestProcessor(serverSocket.accept(), this.handlers));
      }
    } catch (IOException exception) {
      exception.printStackTrace();
    }
  }

  public void stopServer() {
    this.live = false;
  }

  public boolean isLive() {
    return this.live;
  }
}
