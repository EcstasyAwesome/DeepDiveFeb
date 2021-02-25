package academy.kovalevskyi.javadeepdive.week2.day0;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
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
    if (target.getClass().isArray()) {
      builder.append(arrayValuesToJsonEntry(target));
    } else {
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
    }
    return builder.toString();
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromJsonString(String json, Class<T> cls) throws Exception {
    if (json == null || json.matches("^\\s*((\\{\\s*})|(null))*\\s*$")) {
      return null;
    }
    if (cls.isArray() && json.matches("^\\s*\\[\\s*]\\s*$")) {
      return (T) Array.newInstance(cls.getComponentType(), 0);
    }
    final var jsonTrimmed = json.trim();
    final var jsonEntries = jsonTrimmed.substring(1, jsonTrimmed.length() - 1).split(",");
    var result = (T) null;
    if (cls.isArray()) {
      result = (T) Array.newInstance(cls.getComponentType(), jsonEntries.length);
      for (var index = 0; index < jsonEntries.length; index++) {
        var value = jsonEntries[index].trim().replaceAll("\"", "");
        if (cls.getComponentType().equals(String.class)) {
          Array.set(result, index, value);
        } else {
          Array.set(result, index, Integer.parseInt(value));
        }
      }
    } else {
      result = cls.getConstructor().newInstance();
      final var pattern = Pattern.compile("^\\s*(\".+\"\\s*:\\s*.+)\\s*$");
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
    final var length = Array.getLength(obj);
    final var builder = new StringBuilder();
    builder.append('[');
    if (length > 0) {
      for (var index = 0; index < length; index++) {
        builder.append(valueToJsonEntry(Array.get(obj, index))).append(',');
      }
      builder.setCharAt(builder.length() - 1, ']');
    } else {
      builder.append(']');
    }
    return builder.toString();
  }

}