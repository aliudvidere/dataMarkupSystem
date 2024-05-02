package com.markup.dataAccess.util

class EmptinessChecker {

    companion object {
        fun isEmpty(obj: Any?): Boolean {
            if (obj is Collection<Any?>)
                return isEmpty(obj)
            return obj == null
        }

        fun isNotEmpty(obj: Any?): Boolean {
            return !isEmpty(obj)
        }

        private fun isEmpty(obj: Collection<Any?>?): Boolean {
            if (obj == null) {
                return false
            }
            return obj.isEmpty()
        }
    }
}