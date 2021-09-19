package com.example.dddaggregatesample.v2.domain.support

data class OptimisticLockingVersion(val value: Long) {
    init {
        require(value > 0) { "正の値でなければなりません。" }
    }

    companion object {
        fun initial() = OptimisticLockingVersion(1)
    }

    fun next() = OptimisticLockingVersion(value + 1)

    fun isNext(version: OptimisticLockingVersion) = version == this.next()
}

class OptimisticLockingError : Exception()
