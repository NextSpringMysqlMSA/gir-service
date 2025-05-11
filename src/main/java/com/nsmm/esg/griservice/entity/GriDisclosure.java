package com.nsmm.esg.griservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "gri_disclosure",
        uniqueConstraints = @UniqueConstraint(columnNames = {"memberId", "griCode"})
)
public class GriDisclosure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId; // 사용자 식별

    @Column(nullable = false, length = 10)
    private String griCode; // 예: "401-1", "305-3"

    @Column(nullable = false)
    private String indicator; // 지표명 (예: "신규채용자 수 및 이직자 현황")

    @Column(nullable = true)
    private String category; // 예: "고용", "배출" 등

    @Column(columnDefinition = "TEXT")
    private String content; // 사용자가 입력하는 내용

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void updateContent(String newContent) {
        this.content = newContent;
    }
}
