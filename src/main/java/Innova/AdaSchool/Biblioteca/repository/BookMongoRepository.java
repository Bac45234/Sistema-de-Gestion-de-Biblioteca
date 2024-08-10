package Innova.AdaSchool.Biblioteca.repository;

import Innova.AdaSchool.Biblioteca.entity.BookEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookMongoRepository extends MongoRepository<BookEntity, String> {

    @Query("{'author': {$regex: ?0, $options: 'i'}}")
    List<BookEntity> findByAuthor(String author);

    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    List<BookEntity> findByTitle(String title);

    @Query("{'availability': {$regex: ?0}}")
    List<BookEntity> findByAvailability(String availability);

    @Query("{'_id': ?0}")
    @Update("{'$set': {'availability': ?1}}")
    void updateAvailability(String id, String availability);
}