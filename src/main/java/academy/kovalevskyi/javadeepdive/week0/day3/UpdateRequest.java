package academy.kovalevskyi.javadeepdive.week0.day3;

import academy.kovalevskyi.javadeepdive.week0.day2.CSV;
import java.util.Objects;
import java.util.stream.Stream;

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
    final var whereColumn = getColumnId(csv, whereSelector.fieldName());
    final var targetColumn = getColumnId(csv, updateToSelector.fieldName());
    var result = Stream
        .of(csv.values())
        .peek(entry -> {
          if (entry[whereColumn].equals(whereSelector.value())) {
            entry[targetColumn] = updateToSelector.value();
          }
        })
        .toArray(String[][]::new);
    return new CSV.Builder().header(csv.header()).values(result).build();
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
      Objects.requireNonNull(whereSelector);
      Objects.requireNonNull(updateSelector);
      Objects.requireNonNull(csv);
      return new UpdateRequest(csv, whereSelector, updateSelector);
    }
  }
}
