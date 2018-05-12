package main.model

import main.config.Config
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(val username: String, val email: String, val password: String, val permission: Config.PERMISSION) {
    @Id
    lateinit var id: String
    var trust: Double = 0.3
}