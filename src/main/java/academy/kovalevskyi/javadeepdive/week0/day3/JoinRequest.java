package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;

public class JoinRequest extends AbstractRequest<CSV> {

  private final CSV on;
  private final String by;

  private JoinRequest(CSV from, CSV on, String by) {
    super(from);
    this.on = on;
    this.by = by;
  }

  @Override
  protected CSV execute() throws RequestException {
    return null;
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
