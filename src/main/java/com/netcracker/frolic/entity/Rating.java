package com.netcracker.frolic.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Getter
@Setter(AccessLevel.PRIVATE)
@Embeddable
public class Rating implements Comparable<Rating> {
    public static final int MAX_RATING = 100;

    @Column(name="ratings_sum", nullable=false, columnDefinition="int default 0")
    private long ratingsSum;

    @Column(name="ratings_count", nullable=false, columnDefinition="int default 0")
    private long ratingsCount;

    public Rating(long ratingsSum, long ratingsCount) {
        this.ratingsSum = ratingsSum;
        this.ratingsCount = ratingsCount;
    }

    public Rating() { }

    @Transient
    @JsonValue
    public int getRating()
    { return (int) ((ratingsCount == 0) ? 0 : ratingsSum / ratingsCount); }

    public void add(int rating) {
        if (rating < 0  ||  rating > MAX_RATING)
            throw new IllegalArgumentException("This rating is out of boundaries");
        ratingsSum += rating;
        ratingsCount++;
    }

    @Override public String toString()
    { return "Rating: " + getRating(); }

    @Override
    public int compareTo(Rating other) {
        return this.getRating() - other.getRating();
    }
}
