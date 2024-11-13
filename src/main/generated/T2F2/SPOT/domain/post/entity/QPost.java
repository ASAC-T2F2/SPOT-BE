package T2F2.SPOT.domain.post.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -39444865L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final T2F2.SPOT.util.QBaseEntity _super = new T2F2.SPOT.util.QBaseEntity(this);

    public final T2F2.SPOT.domain.category.entity.QCategory category;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<T2F2.SPOT.domain.note.entity.NoteRoom, T2F2.SPOT.domain.note.entity.QNoteRoom> noteRooms = this.<T2F2.SPOT.domain.note.entity.NoteRoom, T2F2.SPOT.domain.note.entity.QNoteRoom>createList("noteRooms", T2F2.SPOT.domain.note.entity.NoteRoom.class, T2F2.SPOT.domain.note.entity.QNoteRoom.class, PathInits.DIRECT2);

    public final EnumPath<T2F2.SPOT.domain.post.PostFor> postFor = createEnum("postFor", T2F2.SPOT.domain.post.PostFor.class);

    public final ListPath<PostImage, QPostImage> postImages = this.<PostImage, QPostImage>createList("postImages", PostImage.class, QPostImage.class, PathInits.DIRECT2);

    public final EnumPath<T2F2.SPOT.domain.post.PostStatus> postStatus = createEnum("postStatus", T2F2.SPOT.domain.post.PostStatus.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final ListPath<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview> reviews = this.<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview>createList("reviews", T2F2.SPOT.domain.review.entity.Review.class, T2F2.SPOT.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    public final T2F2.SPOT.domain.user.entity.QUser user;

    public final ListPath<T2F2.SPOT.domain.wish.entity.Wish, T2F2.SPOT.domain.wish.entity.QWish> wishes = this.<T2F2.SPOT.domain.wish.entity.Wish, T2F2.SPOT.domain.wish.entity.QWish>createList("wishes", T2F2.SPOT.domain.wish.entity.Wish.class, T2F2.SPOT.domain.wish.entity.QWish.class, PathInits.DIRECT2);

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.category = inits.isInitialized("category") ? new T2F2.SPOT.domain.category.entity.QCategory(forProperty("category")) : null;
        this.user = inits.isInitialized("user") ? new T2F2.SPOT.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

