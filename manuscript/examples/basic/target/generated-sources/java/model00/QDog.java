package model00;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDog is a Querydsl query type for Dog
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDog extends EntityPathBase<Dog> {

    private static final long serialVersionUID = -577882985L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDog dog = new QDog("dog");

    public final QBreed breed;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QDog(String variable) {
        this(Dog.class, forVariable(variable), INITS);
    }

    public QDog(Path<? extends Dog> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDog(PathMetadata metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QDog(PathMetadata metadata, PathInits inits) {
        this(Dog.class, metadata, inits);
    }

    public QDog(Class<? extends Dog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.breed = inits.isInitialized("breed") ? new QBreed(forProperty("breed")) : null;
    }

}

