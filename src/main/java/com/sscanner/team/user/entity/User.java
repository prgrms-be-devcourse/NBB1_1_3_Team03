package com.sscanner.team.user.entity;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Builder(toBuilder = true)
@Entity
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE user_id = ?")
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", nullable = false, length = 36, updatable = false)
    private String userId;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "authority", nullable = false, length = 10)
    private String authority;


    @Builder
    public User(String email, String password, String nickname, String phone, String authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phone = phone;
        this.authority = authority;

    }

    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePhone(String newPhone) {
        this.phone = newPhone;
    }

    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public boolean isNicknameEqual(String nickname) {
        return this.nickname.equals(nickname);
    }

    public boolean isPhoneEqual(String phone) {
        return this.phone.equals(phone);
    }

    public String getUserId() {
        return this.userId;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getAuthority() {
        return this.authority;
    }
}