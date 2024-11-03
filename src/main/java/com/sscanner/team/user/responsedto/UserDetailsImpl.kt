package com.sscanner.team.user.responsedto

import com.sscanner.team.user.entity.User
import lombok.RequiredArgsConstructor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@RequiredArgsConstructor
class UserDetailsImpl : UserDetails {
    private val user: User? = null

    //롤 리턴
    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities: MutableCollection<GrantedAuthority> = ArrayList()
        authorities.add(GrantedAuthority { user.getAuthority() })
        return authorities
    }

    override fun getPassword(): String {
        return user.getPassword()
    }

    override fun getUsername(): String {
        return user.getEmail()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}
