package com.ganzi.backend.animal.domain.repository;

import com.ganzi.backend.animal.api.dto.request.AnimalSearchRequest;
import com.ganzi.backend.animal.domain.Animal;
import com.ganzi.backend.animal.domain.ProcessState;
import com.ganzi.backend.animal.domain.QAnimal;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnimalRepositoryCustomImpl implements AnimalRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Animal> searchWithFilters(AnimalSearchRequest request, Pageable pageable) {
        QAnimal animal = QAnimal.animal;

        List<Animal> results = queryFactory
                .selectFrom(animal)
                .where(
                        betweenDate(request.startDate(), request.endDate(), animal),
                        eqProvince(request.province(), animal),
                        eqCity(request.city(), animal),
                        eqAnimalType(request.animalType(), animal),
                        eqSex(request.sex(), animal),
                        eqNeuterStatus(request.neuterStatus(), animal),
                        isProtecting(request.onlyProtecting(), animal)
                )
                .orderBy(getOrderSpecifier(request.isLatest(), animal))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countQuery(request);

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression betweenDate(String startDate, String endDate, QAnimal animal) {
        if (startDate != null && endDate != null) {
            return animal.foundDate.between(startDate, endDate);
        }
        return null;
    }

    private BooleanExpression eqProvince(String province, QAnimal animal) {
        return province != null ? animal.province.eq(province) : null;
    }

    private BooleanExpression eqCity(String city, QAnimal animal) {
        return city != null ? animal.city.eq(city) : null;
    }

    private BooleanExpression eqAnimalType(
            com.ganzi.backend.animal.domain.AnimalType animalType,
            QAnimal animal) {
        return animalType != null ? animal.animalType.eq(animalType) : null;
    }

    private BooleanExpression eqSex(
            com.ganzi.backend.animal.domain.Sex sex,
            QAnimal animal) {
        return sex != null ? animal.sex.eq(sex) : null;
    }

    private BooleanExpression eqNeuterStatus(
            com.ganzi.backend.animal.domain.NeuterStatus neuterStatus,
            QAnimal animal) {
        return neuterStatus != null ? animal.neuterStatus.eq(neuterStatus) : null;
    }

    private BooleanExpression isProtecting(Boolean onlyProtecting, QAnimal animal) {
        return Boolean.TRUE.equals(onlyProtecting)
                ? animal.status.eq(ProcessState.PROTECTING)
                : null;
    }

    private OrderSpecifier<String> getOrderSpecifier(Boolean isLatest, QAnimal animal) {
        return Boolean.TRUE.equals(isLatest)
                ? animal.foundDate.desc()
                : animal.foundDate.asc();
    }

    private long countQuery(AnimalSearchRequest request) {
        QAnimal animal = QAnimal.animal;

        Long count = queryFactory
                .select(animal.count())
                .from(animal)
                .where(
                        betweenDate(request.startDate(), request.endDate(), animal),
                        eqProvince(request.province(), animal),
                        eqCity(request.city(), animal),
                        eqAnimalType(request.animalType(), animal),
                        eqSex(request.sex(), animal),
                        eqNeuterStatus(request.neuterStatus(), animal),
                        isProtecting(request.onlyProtecting(), animal)
                )
                .fetchOne();

        return count != null ? count : 0L;
    }
}
