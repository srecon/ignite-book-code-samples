package com.blu.imdg;

import com.blu.imdg.model.Breed;
import com.blu.imdg.model.Dog;
import com.blu.imdg.repositories.BreedRepository;
import com.blu.imdg.repositories.DogRepository;
import com.blu.imdg.repositories.SpringAppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.Date;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    private static AnnotationConfigApplicationContext ctx;
    private static BreedRepository breedRepository;
    private static DogRepository dogRepository;

    public static void main( String[] args )
    {
        System.out.println( "Spring Data Example!" );
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(SpringAppConfig.class);
        ctx.refresh();

        breedRepository = ctx.getBean(BreedRepository.class);
        dogRepository = ctx.getBean(DogRepository.class);

        //fill the repository with data and Save
        Breed collie = new Breed();
        collie.setId(1L);
        collie.setName("collie");
        //save
        breedRepository.save(1L, collie);

        System.out.println("Add one breed collie in repository!");
        // Query the breed
        List<Breed> getAllBreeds = breedRepository.getAllBreedsByName("collie");

        for(Breed breed : getAllBreeds){
            System.out.println("Breed:" + breed);
        }
        //Add some dogs
        Dog dina = new Dog();
        dina.setName("dina");
        dina.setId(1L);
        dina.setBreedid(1L);
        dina.setBirthdate(new Date(System.currentTimeMillis()));
        //Save Dina
        dogRepository.save(2L,dina);
        System.out.println("Dog dina save into the cache!");
        //Query the Dog Dina
        List<Dog> dogs = dogRepository.getDogByName("dina");
        for(Dog dog : dogs){
            System.out.println("Dog:"+ dog);
        }
        //dina = dogRepository.getDogById(1L);
        //System.out.println("Dina:" + dina);

    }
}
