package main.controller

import main.config.Config
import main.data.ParkingRepo
import main.data.PendingParkingRepo
import main.error.AccessDeniedException
import main.error.UserNotFoundException
import main.manager.UserManager
import main.model.Parking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
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
        for (pendingParking in pendingParkingRepo.findByLocationNear(Point(latitude, longitude), Distance(Config.PARKING_SIZE, Metrics.KILOMETERS)))
        {
            pendingParking.completion += user.trust
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