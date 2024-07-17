package com.lozzato.todolist.user;

import java.util.UUID;

public record UserCreateResponse(String message, UUID userId) {
}
