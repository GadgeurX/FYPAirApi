package main.model

import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class ParkingCluster(location: GeoJsonPoint, indice: Double, val radius: Double): Parking(location, indice) {
    init {
        type = PARKING_TYPE.PARKING_CLUSTER
    }
}