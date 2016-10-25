package nplusone;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Owner implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

//  @OneToMany(mappedBy = "owner")
//  private Set<Dog> dogs;

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

  @Override public String toString() {
    return "Person{" +
      "id=" + id +
      ", name=" + name +
      '}';
  }
}
