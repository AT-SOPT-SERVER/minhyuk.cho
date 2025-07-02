package org.sopt.dto;

import org.sopt.dto.request.PostRequest;

public record PostUpdateDTO(Long id, PostRequest postRequest) {
}
