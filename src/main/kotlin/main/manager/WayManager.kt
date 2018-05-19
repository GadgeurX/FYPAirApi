package main.manager

import main.model.Location
import main.model.Parking
import main.overpass.adapter.OverpassQueryResult
import main.overpass.adapter.OverpassServiceProvider
import main.overpass.output.OutputFormat
import main.overpass.query.OverpassQuery

object WayManager {

    private const val SIZE_SEARCH = 20.0
    private val WAY_TYPE = "way"

    fun getClosestParking(latitude : Double, longitude : Double) : Parking? {
        val query = OverpassQuery().format(OutputFormat.JSON)
                .timeout(30)
                .filterQuery()
                .way()
                .tag("highway")
                .around(SIZE_SEARCH, latitude, longitude)
                .end()
                .output(100)
        val result = OverpassServiceProvider.get().interpreter(query.build()).execute().body()
        var bestParking: Parking? = null
        var bestDistance = Double.MAX_VALUE
        for (element in result.elements)
        {
            if (element.type == WAY_TYPE) {
                val (parking, distance) = getClosestWayParking(result, element, latitude, longitude)
                if (bestDistance > distance) {
                    bestDistance = distance
                    bestParking = parking
                }
            }
        }
        return bestParking
    }

    data class ParkingAndDistance(val parking: Parking?, val distance: Double)
    private fun getClosestWayParking(result: OverpassQueryResult, way : OverpassQueryResult.Element, latitude: Double, longitude: Double) : ParkingAndDistance {
        val nodes = mutableListOf<OverpassQueryResult.Element>()
        for (nodeId in way.nodes) {
            nodes.add(getResultElement(nodeId, result))
        }
        var bestNode : Parking? = null
        var bestDistance = Double.MAX_VALUE
        var i = 0
        while (i < nodes.size - 1) {
            val distance = distanceToSegment(nodes[i], nodes[i + 1], latitude, longitude)
            if (bestDistance > distance) {
                bestDistance = distance
                bestNode = Parking(Location(nodes[i].lat, nodes[i].lon), Location(nodes[i + 1].lat, nodes[i + 1].lon), 0.5)
            }
            i++
        }
        return ParkingAndDistance(bestNode, bestDistance)
    }

    private fun getResultElement(id: Long, result: OverpassQueryResult): OverpassQueryResult.Element {
        for (element in result.elements)
        {
            if (element.id == id) {
                return element
            }
        }
        return result.elements[0]
    }

    /**
     * Return the distance from a point to a segment
     *
     * @param ps,pe the start/end of the segment
     * @param p the given point
     * @return the distance from the given point to the segment
     */
    private fun distanceToSegment(ps: OverpassQueryResult.Element, pe: OverpassQueryResult.Element, pLatitude: Double, pLongitude: Double): Double {

        if (ps.lat == pe.lat && ps.lon == pe.lon) return distance(ps, pLatitude, pLongitude)

        val sx = pe.lat - ps.lat
        val sy = pe.lon - ps.lon

        val ux = pLatitude - ps.lat
        val uy = pLongitude - ps.lon

        val dp = sx * ux + sy * uy
        if (dp < 0) return distance(ps, pLatitude, pLongitude)

        val sn2 = sx * sx + sy * sy
        if (dp > sn2) return distance(pe, pLatitude, pLongitude)

        val ah2 = (dp * dp / sn2)
        val un2 = ux * ux + uy * uy
        return Math.sqrt(un2 - ah2)
    }

    /**
     * return the distance between two points
     *
     * @param p1,p2 the two points
     * @return dist the distance
     */
    private fun distance(p1: OverpassQueryResult.Element, pLatitude: Double, pLongitude: Double): Double {
        val d2 = (pLatitude - p1.lat) * (pLatitude - p1.lat) + (pLongitude - p1.lon) * (pLongitude - p1.lon)
        return Math.sqrt(d2)
    }
}