package modeladv;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Integer id;

  @Column(nullable = false)
  public String name;

  @Column(nullable = false)
  public LocalDate birthdate;

  @Column
  public LocalDate died;

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Dog dog = (Dog) o;

    return id != null ? id.equals(dog.id) : dog.id == null;

  }

  @Override public int hashCode() {
    return id != null ? id.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "Dog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", birthdate='" + birthdate + '\'' +
      ", died='" + died + '\'' +
      '}';
  }
}
