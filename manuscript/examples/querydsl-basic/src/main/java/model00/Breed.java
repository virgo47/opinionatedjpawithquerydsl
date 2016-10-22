package model00;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Breed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  // I don't like this reverse relationship here as I don't believe it's breed's responsibility
  // to care about the dogs in that breed (in other contexts it may make sense, of course)
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

  public Set<Dog> getDogs() {
    return dogs;
  }

  public void setDogs(Set<Dog> dogs) {
    this.dogs = dogs;
  }

  @Override
  public String toString() {
    return "Breed{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
