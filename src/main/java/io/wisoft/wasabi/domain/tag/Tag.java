package io.wisoft.wasabi.domain.tag;

import io.wisoft.wasabi.domain.board.Board;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Tag {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column
    private String name;

    @OneToMany(mappedBy = "tag")
    private Set<Board> boards;

    public Tag(final String name) {
        this.name = name;
        this.boards = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void addBoard(final Board board) {
        this.boards.add(board);
    }

}
