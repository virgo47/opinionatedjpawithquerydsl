package modeladv;

public class LocalizedBreedNamePk {

  public Integer breedId;
  public String langCode;

  public LocalizedBreedNamePk(Integer breedId, String langCode) {
    this.breedId = breedId;
    this.langCode = langCode;
  }
}
