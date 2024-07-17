package com.lozzato.todolist.user;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserRepository repository;
  
  @PostMapping("/")
  public ResponseEntity<UserCreateResponse> createUser(@RequestBody User payload) {
      var user = this.repository.findByUsername(payload.getUsername());

      if (user != null) {
          return ResponseEntity.status(400)
                  .body(new UserCreateResponse("Usuário já existe.", null));
      }

      var hashedPassword = BCrypt.withDefaults().hashToString(12, payload.getPassword().toCharArray());
      payload.setPassword(hashedPassword);

      User newUser = repository.save(payload);

      this.repository.save(newUser);

      return ResponseEntity.ok(new UserCreateResponse("Usuário criado com sucesso.", newUser.getId()));
  }
}
