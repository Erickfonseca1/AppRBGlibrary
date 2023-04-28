package com.example.rgblibrary

class Palette {
    private var list = mutableListOf<Color>()

    fun add(color: Color) {
        this.list.add(color)
    }

    fun get(index: Int): Color {
        return this.list[index]
    }
    fun get(): MutableList<Color>{
        return this.list
    }

    fun delete(index: Int) {
        this.list.removeAt(index)
    }

    fun size(): Int {
        return this.list.size
    }

}