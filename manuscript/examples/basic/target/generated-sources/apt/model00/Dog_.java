package model00;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model00.Breed;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-03-10T22:40:03")
@StaticMetamodel(Dog.class)
public class Dog_ { 

    public static volatile SingularAttribute<Dog, String> name;
    public static volatile SingularAttribute<Dog, Integer> id;
    public static volatile SingularAttribute<Dog, Breed> breed;

}