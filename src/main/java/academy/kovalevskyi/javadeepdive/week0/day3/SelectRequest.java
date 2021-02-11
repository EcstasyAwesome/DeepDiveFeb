package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class SelectRequest extends AbstractRequest<String[][]> {

  private final Selector selector;
  private final String[] columns;

  private SelectRequest(CSV csv, Selector selector, String[] columns) {
    super(csv);
    this.selector = selector;
    this.columns = columns;
  }

  @Override
  protected String[][] execute() throws RequestException {
    final var selected = selector == null ? csv.values() : select(selector);
    final var columns = columns();
    final var result = new String[selected.length][];
    for (var index = 0; index < result.length; index++) {
      result[index] = reduce(selected[index], columns);
    }
    return result;
  }

  private String[][] select(final Selector selector) throws RequestException {
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

  private int[] columns() throws RequestException {
    final var result = new int[columns.length];
    for (var index = 0; index < result.length; index++) {
      result[index] = getColumnId(columns[index]);
    }
    return result;
  }

  private String[] reduce(final String[] value, final int[] columns) {
    final var result = new String[columns.length];
    IntStream
        .range(0, columns.length)
        .forEach(index -> result[index] = value[columns[index]]);
    return result;
  }

  public static class Builder {

    private Selector selector;
    private String[] columns;
    private CSV csv;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder select(String[] columns) {
      this.columns = columns;
      return this;
    }

    public Builder from(CSV csv) {
      this.csv = csv;
      return this;
    }

    public SelectRequest build() {
      Objects.requireNonNull(csv);
      Objects.requireNonNull(columns);
      return new SelectRequest(csv, selector, columns);
    }
  }

}
