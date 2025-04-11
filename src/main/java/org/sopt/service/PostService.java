package org.sopt.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.sopt.domain.Post;
import org.sopt.exception.DuplicateTitleException;
import org.sopt.global.CheckTime;
import org.sopt.repository.PostRepository;

public class PostService {

	private final PostRepository postRepository = new PostRepository();
	private final String pathName = "src/main/java/org/sopt/assets/Post.txt";
	private final File file = new File(pathName);


	public Boolean createPost(Post post){
		if(!findDuplicateTitle(post.getTitle())){
			postRepository.save(post);
			CheckTime.setTimestamp();
			return true;
		}
		return false;
	}

	public List<Post> getAllPosts(){
		return mapToList();
	}

	public Post getPostById(int id){
		Post post = postRepository.findById(id);
		if(post == null){
			return null;
		}
		return postRepository.findById(id);
	}

	public void updatePostById(int id, String newTitle){
		Post post = postRepository.findById(id);
		if(post == null){
			throw new IllegalArgumentException("존재하지 않는 ID입니다.");
		}else if(findDuplicateTitle(newTitle)){
			throw new DuplicateTitleException();
		}
		post.setTitle(newTitle);
	}

	public Boolean deletePostById(int id){
		return postRepository.deleteById(id);
	}

	public List<Post> findPostsByKeyword(String keyword){
		return postRepository.findByKeyword(keyword);
	}

	public void readFromFile(){
		try{
			if(!file.createNewFile()){
				Scanner fsc  = new Scanner(new FileReader(file));
				while(fsc.hasNext()){
					String lines = fsc.nextLine();
					postRepository.save(new Post(lines));
				}
				fsc.close();
			}
		}catch (
			IOException e){
			System.out.println("파일 생성에 오류가 발생하였습니다.");
		}
	}


	public void printToFile(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(Post post : mapToList()){
				writer.write(post.getTitle());
				writer.newLine();
			}
			writer.close();
		} catch(Exception e) {
			System.out.println("파일을 작성하는데 문제가 발생하였습니다.");
		}
	}


	private Boolean findDuplicateTitle(String newTitle){
		for(Post posting : mapToList()){
			if(posting.getTitle().equals(newTitle)){
				return true;
			}
		}
		return false;
	}

	private List<Post> mapToList(){
		List<Post> postList = new ArrayList<>();
		for(Map.Entry<Long,Post> post : postRepository.findAll().entrySet()){
			postList.add(post.getValue());
		}
		return postList;
	}
}
