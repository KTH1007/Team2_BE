package com.ganzi.backend.user.domain;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_likes")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_desertion_no", nullable = false)
    private Animal animal;

    private boolean liked;

    public void updateLiked(boolean liked) {
        this.liked = liked;
    }
}