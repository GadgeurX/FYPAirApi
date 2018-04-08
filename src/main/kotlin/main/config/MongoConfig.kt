package main.config

import com.mongodb.MongoClient
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoConfiguration

@Configuration
open class MongoConfig : AbstractMongoConfiguration() {
    @Throws(Exception::class)
    override fun mongoClient(): MongoClient {
        return MongoClient(Config.MONGO_DB_IP, Config.MONGO_DB_PORT)
    }

    override fun getDatabaseName(): String {
        return Config.MONGO_DB_NAME
    }
}