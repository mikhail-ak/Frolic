package com.netcracker.frolic.repository;

import com.netcracker.frolic.entity.Genre;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<Genre, Long> {
}
