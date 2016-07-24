package model00;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QBreed is a Querydsl query type for Breed
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBreed extends EntityPathBase<Breed> {

    private static final long serialVersionUID = -1296523761L;

    public static final QBreed breed = new QBreed("breed");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QBreed(String variable) {
        super(Breed.class, forVariable(variable));
    }

    public QBreed(Path<? extends Breed> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBreed(PathMetadata metadata) {
        super(Breed.class, metadata);
    }

}

