package com.example.rgblibrary

var ID = 0
class Color(var name: String, var red: Int, var green: Int, var blue: Int): java.io.Serializable {
    var id = ID++

    override fun toString(): String {
        return "$name - R=$red G=$green B=$blue"
    }
}