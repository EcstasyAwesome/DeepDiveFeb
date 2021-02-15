package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

public class JoinRequest extends AbstractRequest<CSV> {

  private final CSV on;
  private final String by;

  private JoinRequest(CSV from, CSV on, String by) {
    super(from);
    this.on = on;
    this.by = by;
  }

  @Override
  public CSV execute() throws RequestException {
    if (!on.withHeader() || !csv.withHeader() || on.values().length != csv.values().length) {
      throw new IllegalArgumentException();
    }
    final var columns = uniqueColumns();
    var header = join(csv.header(), on.header(), columns);
    var values = IntStream
        .range(0, csv.values().length)
        .mapToObj(index -> join(csv.values()[index], on.values()[index], columns))
        .toArray(String[][]::new);
    return new CSV.Builder().header(header).values(values).build();
  }

  private int[] uniqueColumns() throws RequestException {
    final var csvHeader = Arrays.asList(csv.header());
    checkExistingColumn(csvHeader);
    checkExistingColumn(Arrays.asList(on.header()));
    final var result = IntStream
        .range(0, on.header().length)
        .dropWhile(index -> csvHeader.contains(on.header()[index]))
        .toArray();
    if (result.length == 0) {
      throw new RequestException("Csv has no unique columns!");
    }
    return result;
  }

  private static String[] join(final String[] target, final String[] from, final int[] columns) {
    final var result = new String[target.length + columns.length];
    System.arraycopy(target, 0, result, 0, target.length);
    var resultIndex = target.length;
    for (var index : columns) {
      result[resultIndex++] = from[index];
    }
    return result;
  }

  private void checkExistingColumn(final List<String> header) throws RequestException {
    if (!header.contains(by)) {
      throw new RequestException("Common field is absent!");
    }
  }

  public static class Builder {

    private CSV from;
    private CSV on;
    private String by;

    public Builder from(CSV from) {
      this.from = from;
      return this;
    }

    public Builder on(CSV on) {
      this.on = on;
      return this;
    }

    public Builder by(String by) {
      this.by = by;
      return this;
    }

    public JoinRequest build() {
      Objects.requireNonNull(from);
      Objects.requireNonNull(on);
      Objects.requireNonNull(by);
      return new JoinRequest(from, on, by);
    }
  }

}
