package model00;

import javax.persistence.*;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  // lazy is effectively ignored here
  @ManyToOne(fetch = FetchType.LAZY)
  //, cascade = CascadeType.PERSIST) throws exception when using explicit "ghosts" with persist on Hibernate
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
      ", breed.id=" + (breed != null ? breed.getId() : null) +
      '}';
  }
}
