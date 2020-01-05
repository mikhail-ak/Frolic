package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Rating {
    public static final int MAX_RATING = 100;

    @Column(name = "ratings_sum", nullable = false)
    private long ratingsSum;

    @Column(name = "ratings_count", nullable = false)
    private long ratingsCount;

    @JsonValue
    @Formula("ratings_sum / ratings_count")
    private long rating;

    public Rating(long ratingsSum, long ratingsCount) {
        this.ratingsSum = ratingsSum;
        this.ratingsCount = ratingsCount;
    }

    public void add(int rating) {
        if (rating < 0  ||  rating > MAX_RATING)
            throw new IllegalArgumentException("This rating is out of boundaries");
        ratingsSum += rating;
        ratingsCount++;
    }

    @Override
    public String toString()
    { return "Rating: " + rating; }
}
