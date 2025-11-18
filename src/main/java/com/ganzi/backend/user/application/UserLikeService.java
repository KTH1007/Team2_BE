package com.ganzi.backend.user.application;

import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.repository.AnimalRepository;
import com.ganzi.backend.global.code.status.ErrorStatus;
import com.ganzi.backend.global.exception.GeneralException;
import com.ganzi.backend.user.domain.User;
import com.ganzi.backend.user.domain.UserLike;
import com.ganzi.backend.user.domain.repository.UserLikeRepository;
import com.ganzi.backend.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLikeService {

    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final UserLikeRepository userLikeRepository;

    @Transactional
    public void setUserLike(Long userId, String desertionNo, boolean liked) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Animal animal = animalRepository.findByDesertionNo(desertionNo)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANIMAL_NOT_FOUND));

        UserLike userLike = userLikeRepository
                .findByUserIdAndAnimal_DesertionNo(userId, desertionNo)
                .orElseGet(() -> UserLike.builder()
                        .user(user)
                        .animal(animal)
                        .liked(liked)
                        .build());

        if (userLike.getId() != null) {
            userLike.updateLiked(liked);
        }

        userLikeRepository.save(userLike);
    }

    @Transactional(readOnly = true)
    public List<String> getLikedDesertionNos(Long userId) {
        List<UserLike> likes = userLikeRepository.findByUserIdAndLikedTrue(userId);
        return likes.stream()
                .map(like -> like.getAnimal().getDesertionNo())
                .toList();
    }
}