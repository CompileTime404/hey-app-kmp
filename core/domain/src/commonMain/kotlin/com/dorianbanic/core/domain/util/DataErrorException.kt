package com.dorianbanic.core.domain.util

class DataErrorException(
    val error: DataError
): Exception() {
}