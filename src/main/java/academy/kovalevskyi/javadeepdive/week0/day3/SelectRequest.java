package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;

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
    return null;
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
      return new SelectRequest(csv, selector, columns);
    }
  }

}
