package main.config

object Config {
    enum class PERMISSION(val permNumber: Int) {
        ADMIN(0), MOD(1), VIP(2), DEFAULT(3), BAN(4)
    }

    const val MONGO_DB_IP = "127.0.0.1"
    const val MONGO_DB_PORT = 27017
    const val MONGO_DB_NAME = "FYP"

    const val USER_CONNECTION_TIMEOUT: Long = 300000

    const val PARKING_SIZE = 0.05 // in KM
}