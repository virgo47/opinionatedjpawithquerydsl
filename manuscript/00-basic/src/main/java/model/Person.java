package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Person implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private Integer familyId;
  private Integer familySeq;

  @ManyToMany
  @JoinTable(name = "Person_Dog",
    joinColumns = {
      @JoinColumn(name = "fid", referencedColumnName = "familyId"),
      @JoinColumn(name = "fseq", referencedColumnName = "familySeq")
    },
    inverseJoinColumns = @JoinColumn(name = "dog_id", referencedColumnName = "id"))
  private Set<Dog> dogs;

  public void setFamilyIdAndSeq(Integer familyId, Integer familySeq) {
    this.familyId = familyId;
    this.familySeq = familySeq;
  }

  public Integer getFamilyId() {
    return familyId;
  }

  public Integer getFamilySeq() {
    return familySeq;
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
      ", familyId=" + familyId +
      ", familySeq=" + familySeq +
      ", dogs=" + dogs +
      '}';
  }
}
