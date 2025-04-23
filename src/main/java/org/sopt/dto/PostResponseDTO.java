package org.sopt.dto;

public class PostResponseDTO {
	private Long ContentId;

	public PostResponseDTO(Long contentId){
		this.ContentId = contentId;
	}

	public Long getContentId() {
		return ContentId;
	}
}
