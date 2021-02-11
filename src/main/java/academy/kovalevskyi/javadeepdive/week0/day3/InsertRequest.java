package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;

public class InsertRequest extends AbstractRequest<CSV> {

  private final String[] entry;

  private InsertRequest(CSV csv, String[] entry) {
    super(csv);
    this.entry = entry;
  }

  @Override
  protected CSV execute() {
    return new CSV.Builder().header(csv.header()).values(insert(entry)).build();
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
