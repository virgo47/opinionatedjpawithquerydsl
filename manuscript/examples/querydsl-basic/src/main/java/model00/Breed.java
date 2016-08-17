package model00;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Breed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @OneToMany(mappedBy = "breed")
  private Set<Dog> dogs;

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

  @Override
  public String toString() {
    return "Breed{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
