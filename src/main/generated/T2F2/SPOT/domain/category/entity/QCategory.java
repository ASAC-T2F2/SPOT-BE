package T2F2.SPOT.domain.category.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCategory is a Querydsl query type for Category
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategory extends EntityPathBase<Category> {

    private static final long serialVersionUID = -601087301L;

    public static final QCategory category = new QCategory("category");

    public final T2F2.SPOT.util.QBaseEntity _super = new T2F2.SPOT.util.QBaseEntity(this);

    public final StringPath categoryName = createString("categoryName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<T2F2.SPOT.domain.post.entity.Post, T2F2.SPOT.domain.post.entity.QPost> posts = this.<T2F2.SPOT.domain.post.entity.Post, T2F2.SPOT.domain.post.entity.QPost>createList("posts", T2F2.SPOT.domain.post.entity.Post.class, T2F2.SPOT.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public QCategory(String variable) {
        super(Category.class, forVariable(variable));
    }

    public QCategory(Path<? extends Category> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategory(PathMetadata metadata) {
        super(Category.class, metadata);
    }

}

