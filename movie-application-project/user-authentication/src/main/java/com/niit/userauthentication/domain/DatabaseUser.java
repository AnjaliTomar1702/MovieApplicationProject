package com.niit.userauthentication.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
@NoArgsConstructor
@Entity
public class DatabaseUser {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String email;
    @NotBlank
    @JsonIgnore
    private String password;
    @NotNull
    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "database_users_roles", joinColumns = @JoinColumn(
                    name = "database_user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private Image image;

    @Override
    public String toString(){
        return this.email;
    }

}
