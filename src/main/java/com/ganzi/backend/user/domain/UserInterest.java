package com.ganzi.backend.user.domain;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@Table(name = "user_interests")
@NoArgsConstructor
@AllArgsConstructor
public class UserInterest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interest;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "animal_desertion_no")
    private Animal animal;

    private LocalDateTime viewedAt;

    private Integer dwellTimeSeconds;

    private boolean liked;

}
