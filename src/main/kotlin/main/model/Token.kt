package main.model

import main.config.Config
import java.util.*

class Token(var token: String) {
    var timeout: Long = Config.USER_CONNECTION_TIMEOUT
    var creationTime: Long = System.currentTimeMillis()

    constructor() : this(UUID.randomUUID().toString())

    override fun equals(other: Any?): Boolean{
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Token

        if (other.token == this.token) return true

        return false
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}