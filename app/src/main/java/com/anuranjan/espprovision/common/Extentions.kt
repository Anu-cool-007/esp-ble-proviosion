package com.anuranjan.espprovision.common

fun Exception.toAppException() = AppException(message, cause)