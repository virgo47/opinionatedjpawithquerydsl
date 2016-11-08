package nplusone;

import javax.persistence.*;

/** Dog used in eager collection in OwnerEager entity. */
@Entity
@Table(name = "Dog")
public class DogEager {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private OwnerEager owner;

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

  public OwnerEager getOwner() {
    return owner;
  }

  public void setOwner(OwnerEager owner) {
    this.owner = owner;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DogEager dog = (DogEager) o;

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
      ", owner.id=" + (owner != null ? owner.getId() : null) +
      '}';
  }
}
