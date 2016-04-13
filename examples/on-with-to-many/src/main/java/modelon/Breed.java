package modelon;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Breed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String code;

  @OneToMany(mappedBy = "breed")
  private Set<BreedLocalizedName> names;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return "Breed{" +
      "id=" + id +
      ", code='" + code + '\'' +
      '}';
  }
}
