package com.zuhlke.testability.solution

import com.zuhlke.testability.common.TubeLine
import com.zuhlke.testability.common.TubeLineStatus

fun toTubeStatus(tubeStatusResponse: List<Map<String, Any>>): List<TubeLine> {

    return tubeStatusResponse.map { line ->
        val id = line["id"] as String
        val name = line["name"] as String
        val status = (line["lineStatuses"] as List<*>).map { lineStatus ->

            val lineStatusItem = lineStatus as Map<*,*>
            TubeLineStatus(
                (lineStatusItem["statusSeverity"] as Double).toInt(),
                lineStatusItem["statusSeverityDescription"] as String
            )
        }

        TubeLine(id, name, status)
    }
}