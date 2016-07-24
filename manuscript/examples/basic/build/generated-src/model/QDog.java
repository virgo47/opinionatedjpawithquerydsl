package model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = -2011810601L;

    public static final QDog dog = new QDog("dog");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QDog(String variable) {
        super(Dog.class, forVariable(variable));
    }

    public QDog(Path<? extends Dog> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDog(PathMetadata metadata) {
        super(Dog.class, metadata);
    }

}

