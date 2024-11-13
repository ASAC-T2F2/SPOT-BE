package T2F2.SPOT.domain.wish.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWish is a Querydsl query type for Wish
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWish extends EntityPathBase<Wish> {

    private static final long serialVersionUID = -547521523L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWish wish = new QWish("wish");

    public final T2F2.SPOT.util.QBaseEntity _super = new T2F2.SPOT.util.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final T2F2.SPOT.domain.post.entity.QPost post;

    public final T2F2.SPOT.domain.user.entity.QUser user;

    public QWish(String variable) {
        this(Wish.class, forVariable(variable), INITS);
    }

    public QWish(Path<? extends Wish> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWish(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWish(PathMetadata metadata, PathInits inits) {
        this(Wish.class, metadata, inits);
    }

    public QWish(Class<? extends Wish> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new T2F2.SPOT.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new T2F2.SPOT.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

