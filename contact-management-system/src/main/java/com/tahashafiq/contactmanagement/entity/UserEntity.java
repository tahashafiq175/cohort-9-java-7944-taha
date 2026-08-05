package com.tahashafiq.contactmanagement.entity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "User_Table")
public class UserEntity {
    @Id
    String userId;
    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Column(unique = true)
    String userName;

    @Column(unique = true)
    String email;
    @Column(nullable = false)
    String password;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime  createdAt;
    @Column
    private String roles;

    @UpdateTimestamp
    private LocalDateTime  updatedAt;
    @OneToMany(mappedBy = "userEntity", fetch=FetchType.EAGER,cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ContactEntity> contactEntities=new ArrayList<>();

}
