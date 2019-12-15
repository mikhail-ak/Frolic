package com.netcracker.frolic.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Min;

@Getter
@Setter(AccessLevel.PRIVATE)
@Embeddable
public class Rating {
    public static final int MAX_RATING = 100;

    @Column(name="ratings_sum", nullable=false, columnDefinition="int default 0")
    @Min(0)
    private long ratingsSum;

    @Column(name="ratings_count", nullable=false, columnDefinition="int default 0")
    @Min(0)
    private long ratingsCount;

    @Transient
    public long getRating()
    { return (ratingsCount == 0) ? 0 : ratingsSum / ratingsCount; }

    public void add(int rating) {
        if (rating < 0  ||  rating > MAX_RATING)
            throw new IllegalArgumentException("This rating is out of boundaries");
        ratingsSum += rating;
        ratingsCount++;
    }
}
