package model00;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPerson is a Querydsl query type for Person
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QPerson extends EntityPathBase<Person> {

    private static final long serialVersionUID = -1148327398L;

    public static final QPerson person = new QPerson("person");

    public final SetPath<Dog, QDog> dogs = this.<Dog, QDog>createSet("dogs", Dog.class, QDog.class, PathInits.DIRECT2);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> uniqId = createNumber("uniqId", Integer.class);

    public QPerson(String variable) {
        super(Person.class, forVariable(variable));
    }

    public QPerson(Path<? extends Person> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPerson(PathMetadata metadata) {
        super(Person.class, metadata);
    }

}

