package com.netcracker.frolic.service;

import com.google.common.base.CaseFormat;
import com.netcracker.frolic.entity.GameInfo;
import com.netcracker.frolic.entity.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

public interface GameInfoService {
    Optional<GameInfo> findById(long id);
    void deleteById(long id);
    void updateGameInfo(long id, String title, BigDecimal price, GameInfo.Genre genre,
                        Rating rating, String description, Blob logo, LocalDate releaseDate);
    Page<GameInfo> findAllByGenre(GameInfo.Genre genre, Pageable pageable);
    Page<GameInfo> findByTitleContaining(String titlePiece, Pageable pageable);
    Page<GameInfo> findAll(Pageable pageable);
    Optional<GameInfo> save(GameInfo gameInfo);

    public static String enumToCamelCase(Enum<?> enum_)
    { return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, enum_.toString()); }

    enum SortType { RATING, RELEASE_DATE;
        public static Sort resolve(String candidate) {
            if (candidate == null) return Sort.unsorted();
            String camelCaseCandidate = CaseFormat.LOWER_HYPHEN.to(CaseFormat.UPPER_CAMEL, candidate);
            boolean candidateIsValid = Arrays.stream(values())
                    .map(GameInfoService::enumToCamelCase)
                    .anyMatch(camelCaseSortType -> camelCaseSortType.equalsIgnoreCase(camelCaseCandidate));
            return candidateIsValid ? Sort.by(camelCaseCandidate) : Sort.unsorted();
        }
    }

    enum SearchType { GENRE, TITLE;
        public static boolean isPresentAndValid(String candidate) {
            return Arrays.stream(values())
                .map(SearchType::toString)
                .anyMatch(value -> value.equalsIgnoreCase(candidate));
        }
    }
}