package main.manager

import main.config.Config
import main.model.Token
import main.model.User

object UserManager {
    private var connectedUsers = mutableMapOf<Token, User>()

    fun addUser(user : User) : Token {
        val token = Token()
        connectedUsers[token] = user
        return token
    }

    fun getUser(token: Token) :User? {
        return connectedUsers[token]
    }

    fun getUser(token: String) :User? {
        return connectedUsers[Token(token)]
    }

    fun userAllow(user: User, permission: Config.PERMISSION) : Boolean {
        return user.permission >= permission
    }
}