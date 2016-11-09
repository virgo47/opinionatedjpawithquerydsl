package nplusone;

import javax.persistence.*;

/** Raw dog maps owner as the value of FK, that is ownerId. */
@Entity
@Table(name = "Dog")
public class DogRaw {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Column(name = "owner_id")
  private Integer ownerId;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DogRaw dog = (DogRaw) o;

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
      ", ownerId=" + ownerId +
      '}';
  }
}
