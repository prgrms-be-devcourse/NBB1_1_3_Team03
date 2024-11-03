package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(public val user: User) : UserDetails {

    // 유저의 권한을 반환
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add(GrantedAuthority { user.authority })
        return authorities
    }

    // 유저의 비밀번호를 반환
    override fun getPassword(): String {
        return user.password
    }

    // 유저의 이메일(사용자명)을 반환
    override fun getUsername(): String {
        return user.email
    }

    // 계정이 만료되지 않았는지 확인 (항상 true)
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    // 계정이 잠겨 있지 않은지 확인 (항상 true)
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    // 자격 증명이 만료되지 않았는지 확인 (항상 true)
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // 계정이 활성화되어 있는지 확인 (항상 true)
    override fun isEnabled(): Boolean {
        return true
    }
}
