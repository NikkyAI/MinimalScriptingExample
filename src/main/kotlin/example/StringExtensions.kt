package example

fun ByteArray.toHexString(): String = joinToString("", transform = { "%02x".format(it) })