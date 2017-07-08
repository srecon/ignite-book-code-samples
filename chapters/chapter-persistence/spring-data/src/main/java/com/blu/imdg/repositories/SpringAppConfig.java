package com.blu.imdg.repositories;

import com.blu.imdg.model.Breed;
import com.blu.imdg.model.Dog;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by shamim on 08/07/17.
 */
@Configuration
@EnableIgniteRepositories
public class SpringAppConfig {
    @Bean
    public Ignite igniteInstance() {
        IgniteConfiguration cfg = new IgniteConfiguration();

        // Setting some custom name for the node.
        cfg.setIgniteInstanceName("springDataNode");

        // Enabling peer-class loading feature.
        cfg.setPeerClassLoadingEnabled(true);

        // Defining and creating a new cache to be used by Ignite Spring Data
        // repository.
        CacheConfiguration ccfgDog = new CacheConfiguration("DogCache");
        CacheConfiguration ccfgBreed = new CacheConfiguration("BreedCache");

        // Setting SQL schema for the cache.
        ccfgBreed.setIndexedTypes(Long.class, Breed.class);
        ccfgDog.setIndexedTypes(Long.class, Dog.class);


        cfg.setCacheConfiguration(new CacheConfiguration[]{ccfgDog, ccfgBreed});


        return Ignition.start(cfg);
    }
}
