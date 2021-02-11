package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.stream.Stream;

public abstract class AbstractRequest<T> {

  protected CSV csv;

  protected AbstractRequest(CSV csv) {
    this.csv = csv;
  }

  protected abstract T execute() throws RequestException;

  protected int getColumnId(final String field) throws RequestException {
    if (!csv.withHeader()) {
      throw new RequestException("Csv without header!");
    }
    for (var index = 0; index < csv.header().length; index++) {
      if (csv.header()[index].equals(field)) {
        return index;
      }
    }
    throw new RequestException("Csv field has not been found!");
  }

  protected String[][] insert(final String[] entry) {
    final var result = new String[csv.values().length + 1][];
    System.arraycopy(csv.values(), 0, result, 0, csv.values().length);
    result[result.length - 1] = entry;
    return result;
  }

  protected String[][] select(final Selector selector) throws RequestException {
    final var column = getColumnId(selector.fieldName());
    final var result = Stream
        .of(csv.values())
        .filter(entry -> entry[column].equals(selector.value()))
        .toArray(String[][]::new);
    if (result.length == 0) {
      throw new RequestException("Required entries have not been found!");
    }
    return result;
  }
}
