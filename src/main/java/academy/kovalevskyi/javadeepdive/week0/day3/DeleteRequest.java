package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;

public class DeleteRequest extends AbstractRequest<CSV> {

  private final Selector selector;

  private DeleteRequest(CSV csv, Selector selector) {
    super(csv);
    this.selector = selector;
  }

  @Override
  protected CSV execute() throws RequestException {
    return new CSV.Builder().header(csv.header()).values(delete(selector)).build();
  }

  public static class Builder {

    private Selector selector;
    private CSV csv;

    public Builder where(Selector selector) {
      this.selector = selector;
      return this;
    }

    public Builder from(CSV csv) {
      this.csv = csv;
      return this;
    }

    public DeleteRequest build() {
      Objects.requireNonNull(csv);
      Objects.requireNonNull(selector);
      return new DeleteRequest(csv, selector);
    }
  }

}
