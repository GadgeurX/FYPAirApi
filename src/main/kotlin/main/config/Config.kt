package main.config

object Config {
    enum class PERMISSION(val permNumber: Int) {
        ADMIN(0), MOD(1), VIP(2), DEFAULT(3), BAN(4)
    }

    const val USER_CONNECTION_TIMEOUT: Long = 300000
}