package com.scenarioautomation.embrace.team_mobile_base_android.utils

import java.io.InputStream

fun InputStream.readAll(): ByteArray {
    val buffer = ByteArray(2048)
    var result = byteArrayOf()
    do {
        val count = this.read(buffer)
        if (count > 0)
            result += buffer.copyOf(count)
    } while (count >= 0)
    return result
}
