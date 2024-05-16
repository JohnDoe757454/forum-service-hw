package telran.java52.post.service;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java52.post.dao.PostRepository;
import telran.java52.post.dto.DatePeriodDto;
import telran.java52.post.dto.NewCommentDto;
import telran.java52.post.dto.NewPostDto;
import telran.java52.post.dto.PostDto;
import telran.java52.post.model.Comment;
import telran.java52.post.model.Post;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	final PostRepository postRepository;
	final ModelMapper modelMapper;

	@Override
	public PostDto addNewPost(String author, NewPostDto newPostDto) {
		Post post = modelMapper.map(newPostDto, Post.class);
		post.setAuthor(author);
		post = postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto findPostById(String id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto removePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		postRepository.delete(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto updatePost(String id, NewPostDto newPostDto) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		if (newPostDto.getTitle() != null) {
			post.setTitle(newPostDto.getTitle());
		}
		if (newPostDto.getContent() != null) {
			post.setContent(newPostDto.getContent());
		}
		if (newPostDto.getTags() != null) {
			newPostDto.getTags().forEach(post::addTag);
		}
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		Comment comment = modelMapper.map(newCommentDto, Comment.class);
		comment.setUser(author);
		post.addComment(comment);
		postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public void addLike(String id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		post.addLike();
		postRepository.save(post);

	}

	@Override
	public Iterable<PostDto> findPostsByAuthor(String author) {
		return postRepository.findPostsByAuthorIgnoreCase(author).map(p -> modelMapper.map(p, PostDto.class)).toList();
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return postRepository.findPostsByTagsIgnoreCaseIn(tags).map(p -> modelMapper.map(p, PostDto.class)).toList();
	}

	@Override
	public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
		LocalDate from = datePeriodDto.getDateFrom();
		LocalDate to = datePeriodDto.getDateTo();
		return postRepository.findByDateCreatedBetween(from, to).map(p -> modelMapper.map(p, PostDto.class))
				.toList();
	}

}
