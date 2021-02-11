package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

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
    var column = -1;
    for (var entry : csv.header()) {
      if (entry.equals(field)) {
        return ++column;
      }
    }
    throw new RequestException("Csv field has not been found!");
  }

  protected String[][] insert(final String[][] destination, final String[] entry) {
    final var result = new String[destination.length + 1][];
    System.arraycopy(destination, 0, result, 0, destination.length);
    result[result.length - 1] = entry;
    return result;
  }

}
