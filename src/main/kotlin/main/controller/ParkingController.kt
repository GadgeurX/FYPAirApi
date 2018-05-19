package main.controller

import main.config.Config
import main.data.ParkingRepo
import main.data.PendingParkingRepo
import main.error.AccessDeniedException
import main.error.UserNotFoundException
import main.manager.UserManager
import main.manager.WayManager
import main.model.Parking
import main.model.PendingParking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class ParkingController {

    @Autowired
    private lateinit var pendingParkingRepo: PendingParkingRepo
    @Autowired
    private lateinit var parkingRepo: ParkingRepo

    @PostMapping("/parkings")
    @ResponseStatus(value = HttpStatus.OK)
    fun userAddParking(@RequestHeader(value = "token") token: String, @RequestParam(value = "latitude") latitude: Double, @RequestParam(value = "longitude") longitude: Double) {
        val user = UserManager.getUser(token) ?: throw UserNotFoundException()
        if (!UserManager.userAllow(user, Config.PERMISSION.DEFAULT))
            throw AccessDeniedException()
        val parking = WayManager.getClosestParking(latitude, longitude) ?: return
        val parkings = parkingRepo.findByNode1AndNode2(parking.node1, parking.node2)
        if (!parkings.isEmpty())
            return
        val pendingParkings = pendingParkingRepo.findByNode1AndNode2(parking.node1, parking.node2)
        if (pendingParkings.isEmpty()) {
            pendingParkingRepo.insert(PendingParking(parking.node1, parking.node2, 0.5, 0.0))
        }
        for (pendingParking in pendingParkings)
        {
            pendingParking.completion += user.trust
            pendingParkingRepo.save(pendingParking)
            if (pendingParking.completion >= 1) {
                pendingParkingRepo.delete(pendingParking)
                pendingParking.type = Parking.PARKING_TYPE.PARKING
                pendingParking.indice = 0.5
                parkingRepo.insert(pendingParking)
            }
        }
        return
    }

    @GetMapping("/parkings")
    fun getParking() : List<Parking> {
        return parkingRepo.findAll()
    }

}