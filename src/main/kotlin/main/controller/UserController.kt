package main.controller

import main.config.Config
import main.data.UserRepo
import main.error.EmailExistException
import main.error.InvalidUserOrPasswordException
import main.error.UserExistException
import main.model.Token
import main.model.User
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping


@RestController
class UserController {

    @Autowired
    private lateinit var repo: UserRepo
    private var connectedUsers = mutableMapOf<Token, User>()

    @PostMapping("/user")
    fun registerUser(@RequestParam(value = "user") user: String, @RequestParam(value = "email") email: String, @RequestParam(value = "pwd") pwd: String): User {
        val user = User(user, email, pwd, Config.PERMISSION.DEFAULT)
        for (dbUser in repo.findAll()) {
            if (user.email == dbUser.email)
                throw EmailExistException()
            if (user.username == dbUser.username)
                throw UserExistException()
        }
        repo.insert(user)
        return user
    }

    @GetMapping("/login/default")
    fun loginUser(@RequestParam(value = "user") user: String, @RequestParam(value = "pwd") pwd: String): Token {
        for (dbUser in repo.findAll()) {
            if (user == dbUser.username && pwd == dbUser.password)
            {
                val token = Token()
                connectedUsers[token] = dbUser
                return token
            }
        }
        throw InvalidUserOrPasswordException()
    }

}