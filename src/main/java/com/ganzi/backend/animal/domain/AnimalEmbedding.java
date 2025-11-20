package com.ganzi.backend.animal.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "animal_embeddings")
public class AnimalEmbedding {

    @Id
    private String desertionNo;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "desertion_no")
    private Animal animal;

    @Lob
    private String embeddingJson;

    private Integer dimension;

    public void updateEmbedding(String embeddingJson, int dimension) {
        this.embeddingJson = embeddingJson;
        this.dimension = dimension;
    }
}
