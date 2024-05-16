package com.jolivan.archivomotorclasicobackend.User.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {
    @Id
    @GeneratedValue
    private Long id;
    @Column(unique=true)
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String role; // ROLE_ADMIN, ROLE_USER

    private String email;

//    private Set<GrantedAuthority> authorities;

//    public MyUser(String username, String password, Set<? extends GrantedAuthority> authorities) {
//        this.username = username;
//        this.password = password;
//        this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
//    }

//    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
//        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
//        // Ensure array iteration order is predictable (as per
//        // UserDetails.getAuthorities() contract and SEC-717)
//        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<>(new AuthorityComparator());
//        for (GrantedAuthority grantedAuthority : authorities) {
//            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
//            sortedAuthorities.add(grantedAuthority);
//        }
//        return sortedAuthorities;
//    }

//    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
//
//        private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
//
//        @Override
//        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
//            // Neither should ever be null as each entry is checked before adding it to
//            // the set. If the authority is null, it is a custom authority and should
//            // precede others.
//            if (g2.getAuthority() == null) {
//                return -1;
//            }
//            if (g1.getAuthority() == null) {
//                return 1;
//            }
//            return g1.getAuthority().compareTo(g2.getAuthority());
//        }
//
//    }


}
