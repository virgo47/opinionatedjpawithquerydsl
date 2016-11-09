package nplusone;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Raw owner representation does not have mapped collection
 * of dogs (it may have a transient one).
 */
@Entity
@Table(name = "Owner")
public class OwnerRaw implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

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
