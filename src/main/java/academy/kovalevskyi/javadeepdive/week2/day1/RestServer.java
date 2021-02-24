package academy.kovalevskyi.javadeepdive.week2.day1;

import academy.kovalevskyi.javadeepdive.week1.day2.ConcurrentHttpServerWithPath;
import academy.kovalevskyi.javadeepdive.week1.day2.ContentType;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpMethod;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequest;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpRequestsHandler;
import academy.kovalevskyi.javadeepdive.week1.day2.HttpResponse;
import academy.kovalevskyi.javadeepdive.week2.day0.JsonHelper;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;

public class RestServer extends ConcurrentHttpServerWithPath {

  public RestServer(String packagePath) {
    this.addHandlers(initialize(packagePath));
  }

  private List<HttpRequestsHandler> initialize(final String path) {
    return new Reflections(path)
        .getTypesAnnotatedWith(Controller.class)
        .stream()
        .map(this::getHandlers)
        .flatMap(Collection::stream)
        .collect(Collectors.toUnmodifiableList());
  }

  private List<HttpRequestsHandler> getHandlers(final Class<?> clazz) {
    return Stream
        .of(clazz.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(Path.class)
            && (method.isAnnotationPresent(Delete.class)
            || method.isAnnotationPresent(Get.class)
            || method.isAnnotationPresent(Post.class)
            || method.isAnnotationPresent(Put.class)))
        .map(this::getHandler)
        .collect(Collectors.toUnmodifiableList());
  }

  private HttpRequestsHandler getHandler(final Method method) {
    return new HttpRequestsHandler() {
      @Override
      public String path() {
        return method.getAnnotation(Path.class).value();
      }

      @Override
      public HttpMethod method() {
        if (method.isAnnotationPresent(Delete.class)) {
          return HttpMethod.DELETE;
        }
        if (method.isAnnotationPresent(Put.class)) {
          return HttpMethod.PUT;
        }
        if (method.isAnnotationPresent(Post.class)) {
          return HttpMethod.POST;
        }
        return HttpMethod.GET;
      }

      @Override
      public HttpResponse process(HttpRequest request) {
        try {
          final var obj = method.getDeclaringClass().getConstructor().newInstance();
          if (method.isAnnotationPresent(Post.class)) {
            var body = request.body().orElse("");
            method.invoke(obj, JsonHelper.fromJsonString(body, method.getParameterTypes()[0]));
            return new HttpResponse.Builder().body("<h1>Successfully!</h1>").build();
          } else {
            return new HttpResponse.Builder()
                .contentType(ContentType.APPLICATION_JSON)
                .body(JsonHelper.toJsonString(method.invoke(obj)))
                .build();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        return HttpResponse.ERROR_500;
      }
    };
  }
}
