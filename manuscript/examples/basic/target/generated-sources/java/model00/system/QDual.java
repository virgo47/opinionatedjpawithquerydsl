package model00.system;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QDual is a Querydsl query type for Dual
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QDual extends EntityPathBase<Dual> {

    private static final long serialVersionUID = 1276048758L;

    public static final QDual dual = new QDual("dual");

    public final StringPath dummy = createString("dummy");

    public QDual(String variable) {
        super(Dual.class, forVariable(variable));
    }

    public QDual(Path<? extends Dual> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDual(PathMetadata metadata) {
        super(Dual.class, metadata);
    }

}

