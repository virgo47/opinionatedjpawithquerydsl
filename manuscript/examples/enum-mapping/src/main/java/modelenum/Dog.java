package modelenum;


import javax.persistence.*;

@Entity
public class Dog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @Convert(converter = GenderConverter.class)
  @Column(name = "gender")
  private Gender gender;

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
