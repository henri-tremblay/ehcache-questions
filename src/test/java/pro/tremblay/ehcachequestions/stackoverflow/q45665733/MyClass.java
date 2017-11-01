package pro.tremblay.ehcachequestions.stackoverflow.q45665733;

import java.util.Objects;

/**
 * @author Henri Tremblay
 */
public class MyClass {
  private String value;

  public MyClass(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

    @Override
    public boolean equals(Object o) {
      if(this == o) { return true; }
      if(o == null || getClass() != o.getClass()) { return false; }
      MyClass myClass = (MyClass) o;
      return Objects.equals(value, myClass.value);
    }

    @Override
    public int hashCode() {
      return value != null ? value.hashCode() : 0;
    }
}
