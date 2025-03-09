package com.first.demo.domain;

public enum SuggestionStatus {
    PENDING, APPROVED, REJECTED;

    public static SuggestionStatus fromString(String status) {
        try {
            // 문자열을 대문자로 변환하여 처리
            return SuggestionStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // 잘못된 상태 값일 경우 예외 처리
            throw new IllegalArgumentException("유효하지 않은 상태 값입니다: " + status);
        }
    }
}
