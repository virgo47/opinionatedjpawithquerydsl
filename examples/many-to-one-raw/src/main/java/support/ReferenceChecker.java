package support;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class ReferenceChecker {

  private final EntityManager entityManager;

  public ReferenceChecker(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void checkReferences(Object bean) {
    Class<?> beanClass = bean.getClass();
    for (Method method : beanClass.getMethods()) {
      checkReferenceOnMethod(bean, method);
    }

    // NOTE: results of the following while, for and if in checkReferenceOnMethod can be cached

    // loop to check methods/fields in super classes as well, stopping before Object (or some
    // other "layer supertype" - http://martinfowler.com/eaaCatalog/layerSupertype.html)
    while (beanClass != Object.class) {
      for (Field field : beanClass.getDeclaredFields()) {
        checkReferenceOnField(bean, field);
      }
      beanClass = beanClass.getSuperclass();
    }
  }

  private void checkReferenceOnMethod(Object bean, Method method) {
    References referencesAnnotation = method.getAnnotation(References.class);
    if (referencesAnnotation != null) {
      try {
        Serializable objectId = (Serializable) method.invoke(bean);
        findAndInjectRelation(bean, objectId, referencesAnnotation, method);
      } catch (IllegalAccessException | InvocationTargetException | ClassCastException e) {
        throw new RuntimeException( // choose your exception
          "Reflection problem in checkRelations, method=" + method, e);
      }
    }
  }

  private void checkReferenceOnField(Object bean, Field field) {
    References referencesAnnotation = field.getAnnotation(References.class);
    if (referencesAnnotation != null) {
      try {
        field.setAccessible(true);
        Object objectId = field.get(bean);
        findAndInjectRelation(bean, objectId, referencesAnnotation, field);
      } catch (IllegalAccessException e) {
        throw new RuntimeException( // choose your exception
          "Reflection problem in checkRelations, field=" + field, e);
      }
    }
  }

  private void findAndInjectRelation(
    Object bean, Object objectId, References referencesAnnotation, Member member)
  {
    if (objectId != null) {
      checkAndProcessReference(bean, referencesAnnotation, objectId);
    } else if (referencesAnnotation.required()) {
      throw new RuntimeException( // choose your exception
        "Null returned for required value of " + member);
    }
  }

  private void checkAndProcessReference(
    Object bean, References referencesAnnotation, Object objectId)
  {
    Object foundObject = entityManager.find(referencesAnnotation.type(), objectId);
    if (foundObject == null) {
      throw new RuntimeException( // choose your exception
        "Reference not found for " + referencesAnnotation.type() + " and id=" + objectId);
    }

    String targetProperty = referencesAnnotation.targetProperty();
    if (!targetProperty.isEmpty()) {
      PropertyUtils.setValue(bean, targetProperty, foundObject);
    }
    String targetField = referencesAnnotation.targetField();
    if (!targetField.isEmpty()) {
      PropertyUtils.setField(bean, targetField, foundObject);
    }
  }
}

class PropertyUtils {
  public static void setValue(Object bean, String targetProperty, Object foundObject) {
    // TODO use your favourite reflection tools
  }

  public static void setField(Object bean, String targetField, Object foundObject) {
    // TODO use your favourite reflection tools
  }
}
