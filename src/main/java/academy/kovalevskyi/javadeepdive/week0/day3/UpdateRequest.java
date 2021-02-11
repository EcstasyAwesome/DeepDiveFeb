package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;

public class UpdateRequest extends AbstractRequest<CSV> {

  private final Selector whereSelector;
  private final Selector updateToSelector;

  private UpdateRequest(CSV csv, Selector whereSelector, Selector updateToSelector) {
    super(csv);
    this.whereSelector = whereSelector;
    this.updateToSelector = updateToSelector;
  }

  @Override
  protected CSV execute() throws RequestException {
    return null;
  }

  public static class Builder {

    private Selector whereSelector;
    private Selector updateSelector;
    private CSV csv;

    public Builder where(Selector whereSelector) {
      this.whereSelector = whereSelector;
      return this;
    }

    public Builder update(Selector updateSelector) {
      this.updateSelector = updateSelector;
      return this;
    }

    public Builder from(CSV csv) {
      this.csv = csv;
      return this;
    }

    public UpdateRequest build() {
      return new UpdateRequest(csv, whereSelector, updateSelector);
    }
  }
}
