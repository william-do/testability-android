package com.zuhlke.testability.solution

import com.zuhlke.testability.common.TubeLine
import com.zuhlke.testability.common.TubeLineStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TubeStatusTest {

    @Test
    fun `tube status of no data`() {
        val tubeStatus =
            toTubeStatus(emptyList())
        assertTrue(tubeStatus.isEmpty())
    }

    @Test
    fun `tube status of single line`() {
        val tubeStatus = toTubeStatus(
            listOf(
                mapOf(
                    "id" to "my-tube-line",
                    "name" to "My Tube Line",
                    "lineStatuses" to listOf(
                        mapOf(
                            "statusSeverity" to 0.0,
                            "statusSeverityDescription" to "okay"
                        )
                    )
                )
            )
        )

        assertEquals(
            listOf(
                TubeLine(
                    id = "my-tube-line",
                    name = "My Tube Line",
                    statuses = listOf(
                        TubeLineStatus(
                            0,
                            "okay"
                        )
                    )
                )
            ),
            tubeStatus
        )
    }

}
