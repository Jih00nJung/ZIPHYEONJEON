package io.pjj.ziphyeonjeon.interaction.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "LIKES", indexes = {
        @Index(name = "idx_likes_user_house", columnList = "USER_ID, HOUSE_ID")
})
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKES_ID")
    private Long likesId;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "HOUSE_ID")
    private Long houseId;

    // 상가 개발자를 위한 여분 필드
    @Column(name = "STORE_ID")
    private Long storeId;

    @Column(name = "NAME", length = 200)
    private String name;

}
