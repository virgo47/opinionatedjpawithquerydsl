package modeltoone;

import javax.persistence.*;

@Entity
public class Breed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  private Integer derivedFromId;

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

  public Integer getDerivedFromId() {
    return derivedFromId;
  }

  public void setDerivedFromId(Integer derivedFromId) {
    this.derivedFromId = derivedFromId;
  }

  @Override
  public String toString() {
    return "Breed{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
