package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

public class JsonHelper {

  public static <T> String toJsonString(T target) throws IllegalAccessException {
    if (target == null) {
      return "null";
    }
    if (target.getClass().equals(String.class) || target.getClass().equals(Integer.class)) {
      return valueToJsonEntry(target);
    }
    final var builder = new StringBuilder();
    builder.append('{');
    for (var field : target.getClass().getDeclaredFields()) {
      field.setAccessible(true);
      var obj = field.get(target);
      if (obj.getClass().isArray()) {
        builder.append(arrayFieldToJsonEntry(field, obj));
      } else {
        builder.append(fieldToJsonEntry(field)).append(valueToJsonEntry(obj));
      }
      builder.append(',');
    }
    builder.setCharAt(builder.length() - 1, '}');
    return builder.toString();
  }

  public static <T> T fromJsonString(String json, Class<T> cls) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    final var pattern = Pattern.compile("^\\{\\s*(\".+\"\\s*:\\s*.+)\\s*}\\s*$");
    final var splits = json.substring(1, json.length() - 2).split(",");
    var obj = cls.getConstructor().newInstance();
    for (var entry : splits) {
      final var matcher = pattern.matcher(entry);
      if (matcher.find()) {
        var result = matcher.group(1).split(":");
        var field = obj.getClass().getDeclaredField(result[0].trim().replaceAll("\"", ""));
        field.setAccessible(true);
        if (field.getType().equals(String.class)) {
          field.set(obj, result[1].trim().substring(1, result[1].length() - 2));
        } else {
          field.set(obj, Integer.parseInt(result[1].trim()));
        }
      }
    }
    return obj;
  }

  private static String fieldToJsonEntry(final Field field) {
    return String.format("\"%s\":", field.getName());
  }

  private static String valueToJsonEntry(final Object obj) {
    if (obj.getClass().equals(String.class)) {
      return String.format("\"%s\"", obj);
    } else {
      return obj.toString();
    }
  }

  private static String arrayFieldToJsonEntry(final Field field, final Object obj) {
    final var builder = new StringBuilder();
    builder.append(fieldToJsonEntry(field)).append('[');
    if (obj.getClass().equals(int[].class)) {
      var array = (int[]) obj;
      for (var entry : array) {
        builder.append(valueToJsonEntry(entry)).append(',');
      }
    } else {
      var array = (Object[]) obj;
      for (var entry : array) {
        builder.append(valueToJsonEntry(entry)).append(',');
      }
    }
    builder.setCharAt(builder.length() - 1, ']');
    return builder.toString();
  }

}