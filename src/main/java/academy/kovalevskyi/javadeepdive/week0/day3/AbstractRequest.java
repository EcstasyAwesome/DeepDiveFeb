package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.stream.Stream;

public abstract class AbstractRequest<T> {

  protected CSV csv;

  protected AbstractRequest(CSV csv) {
    this.csv = csv;
  }

  protected abstract T execute() throws RequestException;

  protected int getColumnId(final CSV csv, final String field) throws RequestException {
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
}
