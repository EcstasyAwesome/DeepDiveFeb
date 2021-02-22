package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Array;
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
      builder.append(fieldToJsonEntry(field));
      if (obj.getClass().isArray()) {
        builder.append(arrayValuesToJsonEntry(obj));
      } else {
        builder.append(valueToJsonEntry(obj));
      }
      builder.append(',');
    }
    builder.setCharAt(builder.length() - 1, '}');
    return builder.toString();
  }

  public static <T> T fromJsonString(String json, Class<T> cls) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
    final var pattern = Pattern.compile("^\\s*(\".+\"\\s*:\\s*.+)\\s*$");
    final var jsonEntries = json.trim().substring(1, json.length() - 1).split(",");
    final var result = cls.getConstructor().newInstance();
    for (var entry : jsonEntries) {
      final var matcher = pattern.matcher(entry);
      if (matcher.find()) {
        var splits = matcher.group(1).split(":");
        var fieldItem = splits[0].trim().replaceAll("\"", "");
        var valueItem = splits[1].trim().replaceAll("\"", "");
        var field = result.getClass().getDeclaredField(fieldItem);
        field.setAccessible(true);
        if (field.getType().equals(String.class)) {
          field.set(result, valueItem);
        } else {
          field.set(result, Integer.parseInt(valueItem));
        }
      }
    }
    return result;
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

  private static String arrayValuesToJsonEntry(final Object obj) {
    final var builder = new StringBuilder();
    builder.append('[');
    for (var index = 0; index < Array.getLength(obj); index++) {
      builder.append(valueToJsonEntry(Array.get(obj, index))).append(',');
    }
    builder.setCharAt(builder.length() - 1, ']');
    return builder.toString();
  }

}