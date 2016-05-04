package modeltoone;

import javax.persistence.*;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "breed_id", updatable = false, insertable = false)
  private Breed breed;

  @Column(name = "breed_id")
  private Integer breedId;

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
    breedId = breed.getId();
  }

  public Integer getBreedId() {
    return breedId;
  }

  public void setBreedId(Integer breedId) {
    this.breedId = breedId;
  }

  @Override
  public String toString() {
    return "Dog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
