package tests;

import nplusone.Dog;
import nplusone.Owner;

import javax.persistence.EntityManager;

class NPlusOne {

  static void prepareData(EntityManager em) {
    em.getTransaction().begin();

    Owner adam = owner(em, "Adam");
    owner(em, "Charlie");
    Owner joe = owner(em, "Joe");
    Owner mike = owner(em, "Mike");

    dog(em, "Alan", adam);
    dog(em, "Beastie", adam);
    dog(em, "Cessna", adam);
    dog(em, "Rex", joe);
    dog(em, "Lessie", mike);
    dog(em, "Dunco", mike);
    dog(em, "Goro", null);

    em.getTransaction().commit();
  }

  static Owner owner(EntityManager em, String name) {
    Owner owner = new Owner();
    owner.setName(name);
    em.persist(owner);
    em.flush();
    return owner;
  }

  static Dog dog(EntityManager em, String name, Owner owner) {
    Dog dog = new Dog();
    dog.setName(name);
    if (owner != null) {
      dog.setOwner(owner);
    }
    em.persist(dog);
    em.flush();
    return dog;
  }
}
