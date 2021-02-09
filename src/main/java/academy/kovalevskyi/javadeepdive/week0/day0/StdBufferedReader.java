package academy.kovalevskyi.javadeepdive.week0.day0;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;

/**
 * Task w0d0.
 */
public class StdBufferedReader implements Closeable {

  private final Reader reader;
  private boolean ready;
  private boolean emptyBuffer = true;
  private final char[] buffer;
  private char[] storage;
  private int readBytes;
  private int lastLineSeparator;

  public StdBufferedReader(Reader reader, int bufferSize) {
    this.reader = reader;
    this.buffer = new char[bufferSize];
  }

  public StdBufferedReader(Reader reader) {
    this(reader, 1024);
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
    while (true) {
      if (emptyBuffer) {
        if (reader.ready()) {
          readBytes = reader.read(buffer, 0, buffer.length);
          ready = true;
          lastLineSeparator = 0;
        } else {
          ready = false;
          return storage == null ? new char[]{} : storage;
        }
      }
      for (var to = lastLineSeparator; to < readBytes; to++) {
        if (buffer[to] == '\n') {
          final var tmpLastLineSeparator = lastLineSeparator;
          lastLineSeparator = to + 1;
          final var tmpArray = copyArray(buffer, tmpLastLineSeparator, to);
          if (storage == null) {
            if (to != readBytes - 1) {
              emptyBuffer = false;
            }
            return tmpArray;
          } else {
            final var tmpStorage = storage;
            storage = null;
            if (to != readBytes - 1) {
              emptyBuffer = false;
            }
            return mergeArray(tmpStorage, tmpArray, tmpArray.length);
          }
        }
      }
      emptyBuffer = true;
      var tmpArray = copyArray(buffer, lastLineSeparator, readBytes);
      if (storage == null) {
        storage = tmpArray;
      } else {
        storage = mergeArray(storage, tmpArray, tmpArray.length);
      }
    }
  }

  private char[] copyArray(final char[] buffer, final int from, final int to) {
    final var result = new char[to - from];
    System.arraycopy(buffer, from, result, 0, result.length);
    return result;
  }

  private char[] mergeArray(final char[] storage, final char[] buffer, final int realBufferSize) {
    final var result = new char[storage.length + realBufferSize];
    System.arraycopy(storage, 0, result, 0, storage.length);
    System.arraycopy(buffer, 0, result, storage.length, realBufferSize);
    return result;
  }

  public void close() throws IOException {
    reader.close();
  }
}