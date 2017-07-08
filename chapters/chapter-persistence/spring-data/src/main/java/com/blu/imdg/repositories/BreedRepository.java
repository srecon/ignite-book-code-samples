package com.blu.imdg.repositories;

import com.blu.imdg.model.Breed;
import org.apache.ignite.springdata.repository.IgniteRepository;
import org.apache.ignite.springdata.repository.config.Query;
import org.apache.ignite.springdata.repository.config.RepositoryConfig;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by shamim on 08/07/17.
 */
@RepositoryConfig(cacheName = "BreedCache")
public interface BreedRepository extends IgniteRepository<Breed, Long> {

    List<Breed> getAllBreedsByName (String name);

    @Query("SELECT id FROM Breed WHERE id = ?")
    List<Long> getById (long id, Pageable pageable);
}
