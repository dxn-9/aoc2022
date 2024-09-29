package day18

import utils.readFile
import kotlin.test.Test
import kotlin.test.assertEquals

class Solution {
    @Test
    fun solve2() {
        assertEquals(solve2("2,1,2\n1,2,2\n2,3,2\n3,2,2\n2,2,1\n2,2,3"), 30)

        val sample = readFile("day18/SampleInput")
        val input = readFile("day18/Input")
        assertEquals(solve2(sample), 58)
        assertEquals(solve2(input), 2102)
    }
}