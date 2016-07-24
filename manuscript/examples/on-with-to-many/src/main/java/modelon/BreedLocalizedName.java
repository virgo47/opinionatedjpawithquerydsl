package modelon;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;

@Entity
@IdClass(BreedLocalizedNameId.class)
public class BreedLocalizedName {

  @Id
  @ManyToOne
  private Breed breed;

  @Id
  private String language;

  private String name;

  public BreedLocalizedName() {
    // for JPA
  }

  public BreedLocalizedName(Breed breed, String language, String name) {
    this.breed = breed;
    this.language = language;
    this.name = name;
  }
}
