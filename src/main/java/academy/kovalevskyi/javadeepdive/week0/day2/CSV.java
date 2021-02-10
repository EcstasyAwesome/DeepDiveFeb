package academy.kovalevskyi.javadeepdive.week0.day2;

import java.util.Objects;

/**
 * Task w0d2.
 */
public record CSV(String[] header, String[][] values) {

  /**
   * CSV builder.
   */
  public static class Builder {

    private String[] header;
    private String[][] values;

    public Builder header(String[] header) {
      this.header = header;
      return this;
    }

    public Builder values(String[][] values) {
      this.values = values;
      return this;
    }

    public CSV build() {
      Objects.requireNonNull(values);
      return new CSV(header, values);
    }

  }

  public boolean withHeader() {
    return Objects.nonNull(header);
  }

  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(CSV.class)) {
      return Objects.equals(this, o);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hash(header, values);
  }

}
