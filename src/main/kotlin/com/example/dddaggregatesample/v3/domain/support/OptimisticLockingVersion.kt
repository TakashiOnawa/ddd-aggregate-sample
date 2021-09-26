package com.example.dddaggregatesample.v3.domain.support

data class OptimisticLockingVersion(val value: Long) {
    init {
        require(value > 0) { "正の値でなければなりません。" }
    }

    companion object {
        fun initial() = OptimisticLockingVersion(1)
        fun of(value: Long?) = if (value == null) initial() else OptimisticLockingVersion(value)
    }

    fun next() = OptimisticLockingVersion(value + 1)

    fun isNext(version: OptimisticLockingVersion) = this == version.next()
}

class OptimisticLockingError : Exception()
