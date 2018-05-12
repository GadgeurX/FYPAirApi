package main.data

import main.model.Parking
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point


interface ParkingRepo : MongoRepository<Parking, String> {
    fun findByLocationNear(p: Point, d: Distance): List<Parking>
}