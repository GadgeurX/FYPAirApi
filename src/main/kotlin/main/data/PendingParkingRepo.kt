package main.data

import main.model.Location
import main.model.PendingParking
import org.springframework.data.mongodb.repository.MongoRepository

interface PendingParkingRepo : MongoRepository<PendingParking, String> {
    fun findByNode1AndNode2(node1: Location, node2: Location): List<PendingParking>
}