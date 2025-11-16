package com.ganzi.backend.user.domain;

import com.ganzi.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "user_embeddings")
@NoArgsConstructor
@AllArgsConstructor
public class UserEmbedding extends BaseEntity {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    @Column(name="embedding_json", columnDefinition = "TEXT")
    private String embeddingJson;

    @Column(name = "dimension")
    private Integer dimension;

    private LocalDateTime updatedAt;

    public void updateUserEmbedding(String json, int dimension) {
        this.embeddingJson = json;
        this.dimension = dimension;
        this.updatedAt = LocalDateTime.now();
    }
}
