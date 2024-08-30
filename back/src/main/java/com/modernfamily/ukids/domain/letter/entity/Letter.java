package com.modernfamily.ukids.domain.letter.entity;

import com.modernfamily.ukids.domain.tree.entity.Tree;
import com.modernfamily.ukids.domain.user.entity.User;
import com.modernfamily.ukids.global.baseTimeEntity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@NoArgsConstructor
@Getter
public class Letter extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterId;

    @Column(nullable = false, length = 3000)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tree_id", nullable = false)
    private Tree tree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", referencedColumnName = "user_id", nullable = false)
    private User fromUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", referencedColumnName = "user_id", nullable = false)
    private User toUser;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDelete;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isOpen;

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isRead;

    @Builder
    private Letter(String content, Tree tree, User fromUser, User toUser, boolean isDelete, boolean isOpen, boolean isRead) {
        this.content = content;
        this.tree = tree;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.isDelete = isDelete;
        this.isOpen = isOpen;
        this.isRead = isRead;
    }

    public static Letter createLetter(String content, Tree tree, User fromUser, User toUser) {
        return Letter.builder()
                .content(content)
                .tree(tree)
                .fromUser(fromUser)
                .toUser(toUser)
                .isDelete(false)
                .isOpen(false)
                .isRead(false)
                .build();
    }

    public void readLetter() {
        this.isRead = true;
    }

    public void deleteLetter() {
        this.isDelete = true;
    }
}
