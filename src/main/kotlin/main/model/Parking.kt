package main.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document

@Document
open class Parking(val node1: Location, val node2: Location, var indice: Double) {
    enum class PARKING_TYPE {
        PARKING, PENDING_PARKING, PARKING_CLUSTER
    }

    @Id
    lateinit var id: String
    var type = PARKING_TYPE.PARKING
}