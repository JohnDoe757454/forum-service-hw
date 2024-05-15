package telran.java52.post.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import telran.java52.post.model.Post;

public interface PostRepository extends MongoRepository<Post, String> {

	Stream<Post> findPostsByAuthorIgnoreCase(String author);

	Stream<Post> findPostsByTagsIgnoreCaseIn(List<String> tags);
	
	@Query("{'dateCreated' :{$gt:?0,$lt:?1}}")
	Stream<Post> findPostsBetweenDateCreated(LocalDate from, LocalDate to);
}
