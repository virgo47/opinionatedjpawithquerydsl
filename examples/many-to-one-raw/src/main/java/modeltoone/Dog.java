package modeltoone;

import support.DaoUtils;
import support.References;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @References(type = Breed.class, required = true)
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

  public Integer getBreedId() {
    return breedId;
  }

  public void setBreedId(Integer breedId) {
    this.breedId = breedId;
  }

  public Breed getBreed() {
    return DaoUtils.load(Breed.class, breedId);
  }

  @Override
  public String toString() {
    return "Dog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
