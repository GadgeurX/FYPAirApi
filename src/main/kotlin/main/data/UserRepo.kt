package main.data

import main.model.User
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepo : MongoRepository<User, String> {
    fun findByUsername(username : String) : List<User>
}