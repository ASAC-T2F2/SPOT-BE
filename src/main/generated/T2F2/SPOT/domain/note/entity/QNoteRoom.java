package T2F2.SPOT.domain.note.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QNoteRoom is a Querydsl query type for NoteRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNoteRoom extends EntityPathBase<NoteRoom> {

    private static final long serialVersionUID = -721089122L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QNoteRoom noteRoom = new QNoteRoom("noteRoom");

    public final T2F2.SPOT.util.QBaseEntity _super = new T2F2.SPOT.util.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final T2F2.SPOT.domain.user.entity.QUser guest;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<Note, QNote> notes = this.<Note, QNote>createList("notes", Note.class, QNote.class, PathInits.DIRECT2);

    public final T2F2.SPOT.domain.user.entity.QUser owner;

    public final T2F2.SPOT.domain.post.entity.QPost post;

    public QNoteRoom(String variable) {
        this(NoteRoom.class, forVariable(variable), INITS);
    }

    public QNoteRoom(Path<? extends NoteRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QNoteRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QNoteRoom(PathMetadata metadata, PathInits inits) {
        this(NoteRoom.class, metadata, inits);
    }

    public QNoteRoom(Class<? extends NoteRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.guest = inits.isInitialized("guest") ? new T2F2.SPOT.domain.user.entity.QUser(forProperty("guest")) : null;
        this.owner = inits.isInitialized("owner") ? new T2F2.SPOT.domain.user.entity.QUser(forProperty("owner")) : null;
        this.post = inits.isInitialized("post") ? new T2F2.SPOT.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

