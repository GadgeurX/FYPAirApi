package main.model

import main.config.Config
import java.util.*

class Token {
    var token: String = UUID.randomUUID().toString()
    var timeout: Long = Config.USER_CONNECTION_TIMEOUT
    var creationTime: Long = System.currentTimeMillis()
}