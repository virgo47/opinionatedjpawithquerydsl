package modeladv;

import java.io.Serializable;

public class LocalizedBreedNamePk implements Serializable {

  public Integer breedId;
  public String langCode;

  public LocalizedBreedNamePk() {
    // EclipseLink does need this one, but Hibernate does
  }

  public LocalizedBreedNamePk(Integer breedId, String langCode) {
    this.breedId = breedId;
    this.langCode = langCode;
  }
}
