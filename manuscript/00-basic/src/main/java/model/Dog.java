package model;

import javax.persistence.*;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "breed_id")
  private Breed breed;

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

  public Breed getBreed() {
    return breed;
  }

  public void setBreed(Breed breed) {
    this.breed = breed;
  }

  @Override
  public String toString() {
    return "Dog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      ", breed.id=" + (breed != null ? breed.getId() : null) +
      '}';
  }
}
