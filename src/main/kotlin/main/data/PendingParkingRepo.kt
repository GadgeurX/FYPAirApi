package main.data

import main.model.Parking
import main.model.PendingParking
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point

interface PendingParkingRepo : MongoRepository<PendingParking, String> {
    fun findByLocationNear(p: Point, d: Distance): List<PendingParking>
}