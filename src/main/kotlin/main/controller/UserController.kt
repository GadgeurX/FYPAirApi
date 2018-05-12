package main.controller

import main.config.Config
import main.data.UserRepo
import main.error.EmailExistException
import main.error.InvalidUserOrPasswordException
import main.error.UserExistException
import main.manager.UserManager
import main.model.Token
import main.model.User
import main.utils.CryptoHelper
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping


@RestController
class UserController {

    @Autowired
    private lateinit var repo: UserRepo

    @PostMapping("/users")
    fun registerUser(@RequestParam(value = "user") username: String, @RequestParam(value = "email") email: String, @RequestParam(value = "pwd") pwd: String): User {
        val user = User(username, email, CryptoHelper.sha256(pwd), Config.PERMISSION.DEFAULT)
        for (dbUser in repo.findAll()) {
            if (user.email == dbUser.email)
                throw EmailExistException()
            if (user.username == dbUser.username)
                throw UserExistException()
        }
        repo.insert(user)
        return user
    }

    @GetMapping("/users/default")
    fun loginUser(@RequestParam(value = "user") username: String, @RequestParam(value = "pwd") pwd: String): Token {
        for (dbUser in repo.findByUsername(username)) {
            if (username == dbUser.username && CryptoHelper.sha256(pwd) == dbUser.password)
                return UserManager.addUser(dbUser)
        }
        throw InvalidUserOrPasswordException()
    }

}