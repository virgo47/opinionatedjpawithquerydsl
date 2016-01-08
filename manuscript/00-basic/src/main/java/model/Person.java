package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Person implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer uniqId;

  @ManyToMany
  @JoinTable(name = "Person_Dog",
    joinColumns = @JoinColumn(name = "puid", referencedColumnName = "uniqId"),
    inverseJoinColumns = @JoinColumn(name = "dog_id", referencedColumnName = "id"))
  private Set<Dog> dogs;

  public void setUniqId(Integer uniqId) {
    this.uniqId = uniqId;
  }

  public Set<Dog> getDogs() {
    return dogs;
  }

  public void setDogs(Set<Dog> dogs) {
    this.dogs = dogs;
  }

  @Override public String toString() {
    return "Person{" +
      "id=" + id +
      ", uniqId=" + uniqId +
      ", dogs=" + dogs +
      '}';
  }
}
