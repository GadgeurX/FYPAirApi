package main.model

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document
class PendingParking(location: GeoJsonPoint, indice: Double, var completion : Double) : Parking(location, indice) {
    init {
        type = PARKING_TYPE.PENDING_PARKING
    }
}