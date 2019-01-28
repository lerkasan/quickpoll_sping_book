package ua.com.foxminded.lerkasan.quickpoll.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty
    @JsonIgnore
    private String password;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Column(columnDefinition = "char(3)")
    @Type(type = "yes_no")
    @NotEmpty
    private boolean admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return admin ? AuthorityUtils.createAuthorityList("ROLE_ADMIN") : AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
