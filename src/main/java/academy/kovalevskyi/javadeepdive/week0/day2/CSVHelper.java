package academy.kovalevskyi.javadeepdive.week0.day2;

import academy.kovalevskyi.javadeepdive.week0.day0.StdBufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Task w0d2.
 */
public class CSVHelper {

  /**
   * Parses csv from some stream.
   *
   * @param reader stream
   * @return csv object
   * @throws IOException some i/o error
   */
  public static CSV parseFile(Reader reader) throws IOException {
    return parseFile(reader, false, ',');
  }

  /**
   * Parses csv from some stream with parameters.
   *
   * @param reader     stream
   * @param withHeader parse header or not
   * @param delimiter  delimiter
   * @return csv object
   * @throws IOException some i/o error
   */
  public static CSV parseFile(Reader reader, boolean withHeader, char delimiter)
      throws IOException {
    final var result = new CSV.Builder();
    final var entries = new ArrayList<String[]>();
    try (final var buffReader = new StdBufferedReader(reader)) {
      var readHeader = false;
      while (buffReader.hasNext()) {
        var text = buffReader.line();
        if (text.trim().isEmpty()) {
          continue;
        }
        if (withHeader && !readHeader) {
          readHeader = true;
          result.header(text.split(String.valueOf(delimiter)));
        } else {
          entries.add(text.split(String.valueOf(delimiter)));
        }
      }
    }
    return result.values(entries.toArray(String[][]::new)).build();
  }

  /**
   * Writes csv to some stream.
   *
   * @param writer    stream
   * @param csv       csv
   * @param delimiter delimiter
   * @throws IOException some i/o error
   */
  public static void writeCsv(Writer writer, CSV csv, char delimiter) throws IOException {
    try (writer) {
      if (csv.withHeader()) {
        writer.write(prepareLine(csv.header(), delimiter));
      }
      for (var entry : csv.values()) {
        writer.write(prepareLine(entry, delimiter));
      }
    }
  }

  private static String prepareLine(final String[] array, final char delimiter) {
    return String.format("%s\n", String.join(String.valueOf(delimiter), array));
  }
}
