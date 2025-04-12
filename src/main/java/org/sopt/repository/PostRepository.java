package org.sopt.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.sopt.domain.Post;
import org.sopt.utils.IDGenUtil;

public class PostRepository {

	Map<Long,Post> postMap = new HashMap<>();
	private final String pathName = "src/main/java/org/sopt/assets/Post.txt";
	private final File file = new File(pathName);

	public void save(Post post) {
		postMap.put(post.getId(),post);
	}

	public Map<Long,Post> findAll(){
		return postMap;
	}

	public Post findById(long id) {
		return postMap.get(id);
	}

	public List<Post> findByKeyword(String keyword){
		List<Post> newList = new ArrayList<>();
		for(Map.Entry<Long,Post> post : postMap.entrySet()){
			if(post.getValue().getTitle().contains(keyword)){
				newList.add(post.getValue());
			}
		}
		return newList;
	}

	public Boolean deleteById(long id) {
		Post post = postMap.remove(id);
		return post != null;
	}

	public void readFromFile(){
		try{
			if(!file.createNewFile()){
				Scanner fsc  = new Scanner(new FileReader(file));
				long number = 0;
				while(fsc.hasNext()){
					String lines = fsc.nextLine();
					lines = lines.trim();
					String[] parts = lines.split(" ");
					Post post = new Post(parts[1]);
					post.setId(Integer.parseInt(parts[0]));
					number = Integer.parseInt(parts[0]);
					save(post);
				}
				IDGenUtil.setId(number);
				fsc.close();
			}
		}catch (
			IOException e){
			System.out.println("파일 생성에 오류가 발생하였습니다.");
		}
	}

	public void writeToFile(){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for(Map.Entry<Long,Post> post : postMap.entrySet()){
				writer.write(post.getValue().getId().toString());
				writer.write(" ");
				writer.write(post.getValue().getTitle());
				writer.newLine();
			}
			writer.close();
		} catch(Exception e) {
			System.out.println("파일을 작성하는데 문제가 발생하였습니다.");
		}
	}
}
