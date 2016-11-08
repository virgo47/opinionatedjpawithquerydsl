package nplusone;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/** Owner variant with eager collection for dogs. */
@Entity
@Table(name = "Owner")
public class OwnerEager implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
  private Set<DogEager> dogs;

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

  public Set<DogEager> getDogs() {
    return dogs;
  }

  @Override public String toString() {
    return "Person{" +
      "id=" + id +
      ", name=" + name +
      '}';
  }
}
