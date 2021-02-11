package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;

public class InsertRequest extends AbstractRequest<CSV> {

  private final String[] entries;

  private InsertRequest(CSV csv, String[] entries) {
    super(csv);
    this.entries = entries;
  }

  @Override
  protected CSV execute() {
    final var entries = insert(csv.values(), this.entries);
    return new CSV.Builder().header(csv.header()).values(entries).build();
  }

  public static class Builder {

    private String[] entries;
    private CSV csv;

    public Builder insert(String[] entries) {
      this.entries = entries;
      return this;
    }

    public Builder to(CSV csv) {
      this.csv = csv;
      return this;
    }

    public InsertRequest build() {
      Objects.requireNonNull(csv);
      Objects.requireNonNull(entries);
      return new InsertRequest(csv, entries);
    }
  }

}
