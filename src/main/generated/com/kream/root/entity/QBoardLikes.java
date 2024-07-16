package com.kream.root.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardLikes is a Querydsl query type for BoardLikes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardLikes extends EntityPathBase<BoardLikes> {

    private static final long serialVersionUID = -754935642L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardLikes boardLikes = new QBoardLikes("boardLikes");

    public final QBoard board;

    public final QBoardLikesId id;

    public final ComparablePath<Character> likeDislike = createComparable("likeDislike", Character.class);

    public final com.kream.root.Login.model.QUserListDTO user;

    public QBoardLikes(String variable) {
        this(BoardLikes.class, forVariable(variable), INITS);
    }

    public QBoardLikes(Path<? extends BoardLikes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardLikes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardLikes(PathMetadata metadata, PathInits inits) {
        this(BoardLikes.class, metadata, inits);
    }

    public QBoardLikes(Class<? extends BoardLikes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.id = inits.isInitialized("id") ? new QBoardLikesId(forProperty("id")) : null;
        this.user = inits.isInitialized("user") ? new com.kream.root.Login.model.QUserListDTO(forProperty("user"), inits.get("user")) : null;
    }

}

