package com.lozzato.todolist.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.print.DocFlavor;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

  @CreationTimestamp
  private LocalDateTime createdAt;
}
