package support;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class DaoUtils {
  public static <T> T load(Class<T> entityClass, Serializable entityId) {
    EntityManager em = null; // TODO get it somehow (depends on the project)
    return em.find(entityClass, entityId);
  }
}
