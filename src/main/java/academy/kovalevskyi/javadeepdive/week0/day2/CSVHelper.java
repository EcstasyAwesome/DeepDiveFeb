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
    String headerTemplate = String.format("^name%1$cbirthday_year%1$ccomment$", delimiter);
    String entryTemplate = String.format("^.*%1$c.*%1$c.*$", delimiter);
    try (final var buffReader = new StdBufferedReader(reader)) {
      while (buffReader.hasNext()) {
        var text = String.valueOf(buffReader.readLine());
        if (withHeader && text.matches(headerTemplate)) {
          result.header(text.split(String.valueOf(delimiter)));
        } else if (text.matches(entryTemplate)) {
          entries.add(text.split(String.valueOf(delimiter)));
        }
      }
      result.values(entries.toArray(String[][]::new));
    }
    return result.build();
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
        writer.write(String.join(String.valueOf(delimiter), csv.header()));
        writer.write('\n');
      }
      for (var entry : csv.values()) {
        writer.write(String.join(String.valueOf(delimiter), entry));
        writer.write('\n');
      }
      writer.flush();
    }
  }
}
