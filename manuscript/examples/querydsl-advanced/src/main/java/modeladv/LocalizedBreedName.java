package modeladv;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(LocalizedBreedNamePk.class)
public class LocalizedBreedName {

  @Id
  private Integer breedId;

  @Id
  private String langCode;

  private String name;

  public LocalizedBreedName() {
  }

  public LocalizedBreedName(Integer breedId, String langCode, String name) {
    this.breedId = breedId;
    this.langCode = langCode;
    this.name = name;
  }


  @Override
  public String toString() {
    return "LocalizedBreedName{" +
      "breedId=" + breedId +
      ", langCode='" + langCode + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
