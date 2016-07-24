package model00;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model00.Dog;

@Generated(value="EclipseLink-2.6.2.v20151217-rNA", date="2016-03-10T22:40:03")
@StaticMetamodel(Person.class)
public class Person_ { 

    public static volatile SingularAttribute<Person, String> name;
    public static volatile SetAttribute<Person, Dog> dogs;
    public static volatile SingularAttribute<Person, Integer> uniqId;
    public static volatile SingularAttribute<Person, Integer> id;

}