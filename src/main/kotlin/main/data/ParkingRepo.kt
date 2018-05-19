package main.data

import main.model.Location
import main.model.Parking
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Point


interface ParkingRepo : MongoRepository<Parking, String> {
    fun findByNode1AndNode2(node1: Location, node2: Location): List<Parking>
}