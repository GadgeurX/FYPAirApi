package main.model

import org.springframework.data.mongodb.core.mapping.Document

@Document
class PendingParking(node1: Location, node2: Location, indice: Double, var completion : Double) : Parking(node1, node2, indice) {
    init {
        type = PARKING_TYPE.PENDING_PARKING
    }
}