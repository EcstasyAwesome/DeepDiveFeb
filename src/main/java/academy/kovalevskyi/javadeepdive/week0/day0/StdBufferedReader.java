package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * Task w0d0.
 */
public class StdBufferedReader implements Closeable {

  private final Reader reader;
  private final int bufferSize;
  private boolean ready;

  public StdBufferedReader(Reader reader, int bufferSize) {
    this.reader = reader;
    this.bufferSize = bufferSize;
  }

  public StdBufferedReader(Reader reader) {
    this.reader = reader;
    this.bufferSize = 100;
  }

  public boolean hasNext() throws IOException {
    return reader.ready() || ready;
  }

  /**
   * Reads a line of text. A line is considered to be terminated by any one of a line feed ('\n'), a
   * carriage return ('\r'), a carriage return followed immediately by a line feed, or by reaching
   * the end-of-file (EOF).
   *
   * @return set of chars
   * @throws IOException if an I/O error occurs
   */
  public char[] readLine() throws IOException {
    StringBuilder result = new StringBuilder(this.bufferSize);

    while (ready = reader.ready()) {
      char symbol = (char) reader.read();
      if (symbol == '\n') {
        return result.toString().toCharArray();
      }
      result.append(symbol);
    }
    return result.toString().toCharArray();
  }

  public void close() throws IOException {
    reader.close();
  }
}