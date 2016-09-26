package modeltoone;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Dog")
public class DogBasicView {

  @Id
  @Column(updatable = false)
  private Integer id;

  private String name;

  // DON'T map additional attributes if you auto-generate schema! Keep your views pure sub-sets of
  // the main entity. Auto-generator doesn't create union of all attributes, it doesn't expect it.
  // private String description;

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
    return "Dog{" +
      "id=" + id +
      ", name='" + name + '\'' +
      '}';
  }
}
