package com.picus.core.shared.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
@JsonPropertyOrder({"timestamp", "code", "message", "result"}) // JSON 응답 시 순서를 정의
public class BaseResponse<T> {

    private final LocalDateTime timestamp = LocalDateTime.now();

    private final String code;

    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    //성공한 경우 응답 생성
    public static <T> BaseResponse<T> onSuccess(T result) {
        return new BaseResponse<>("COMMON200", "요청에 성공하였습니다.", result);
    }

    public static BaseResponse<Void> onSuccess() {
        return new BaseResponse<>("COMMON200", "요청에 성공하였습니다.", null);
    }

    // 실패한 경우 응답 생성
    public static <T> BaseResponse<T> onFailure(String code, String message, T data) {
        return new BaseResponse<>(code, message, data);
    }

}
