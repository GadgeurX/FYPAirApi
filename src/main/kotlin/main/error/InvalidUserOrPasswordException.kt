package main.error

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "invalid_user_or_password")
class InvalidUserOrPasswordException: RuntimeException()