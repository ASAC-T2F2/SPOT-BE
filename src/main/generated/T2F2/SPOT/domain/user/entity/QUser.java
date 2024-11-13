package T2F2.SPOT.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1782031275L;

    public static final QUser user = new QUser("user");

    public final T2F2.SPOT.util.QBaseEntity _super = new T2F2.SPOT.util.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final StringPath entranceYear = createString("entranceYear");

    public final EnumPath<T2F2.SPOT.domain.user.grade.Grade> grade = createEnum("grade", T2F2.SPOT.domain.user.grade.Grade.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath major = createString("major");

    public final NumberPath<Float> mannerScore = createNumber("mannerScore", Float.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final ListPath<T2F2.SPOT.domain.post.entity.Post, T2F2.SPOT.domain.post.entity.QPost> posts = this.<T2F2.SPOT.domain.post.entity.Post, T2F2.SPOT.domain.post.entity.QPost>createList("posts", T2F2.SPOT.domain.post.entity.Post.class, T2F2.SPOT.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final ListPath<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview> receivedReviews = this.<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview>createList("receivedReviews", T2F2.SPOT.domain.review.entity.Review.class, T2F2.SPOT.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final EnumPath<T2F2.SPOT.domain.user.Role> role = createEnum("role", T2F2.SPOT.domain.user.Role.class);

    public final ListPath<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview> sendReviews = this.<T2F2.SPOT.domain.review.entity.Review, T2F2.SPOT.domain.review.entity.QReview>createList("sendReviews", T2F2.SPOT.domain.review.entity.Review.class, T2F2.SPOT.domain.review.entity.QReview.class, PathInits.DIRECT2);

    public final StringPath university = createString("university");

    public final ListPath<T2F2.SPOT.domain.wish.entity.Wish, T2F2.SPOT.domain.wish.entity.QWish> wishes = this.<T2F2.SPOT.domain.wish.entity.Wish, T2F2.SPOT.domain.wish.entity.QWish>createList("wishes", T2F2.SPOT.domain.wish.entity.Wish.class, T2F2.SPOT.domain.wish.entity.QWish.class, PathInits.DIRECT2);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

