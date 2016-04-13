package modelon;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BreedLocalizedNameId implements Serializable {

  public Integer breed;

  public String language;
}
