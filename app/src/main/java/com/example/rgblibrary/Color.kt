package com.example.rgblibrary

class Color(var name: String, var red: Int, var green: Int, var blue: Int): java.io.Serializable {
    companion object {
        private var nextId = 0
    }
    val id = nextId++

    override fun toString(): String {
        return "$name - R=$red G=$green B=$blue"
    }
}