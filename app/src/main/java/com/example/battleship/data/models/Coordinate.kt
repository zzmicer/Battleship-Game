package com.example.battleship.data.models

class Coordinate (val x: Int, val y: Int){

    fun isSameCoordinate (coord: Coordinate): Boolean {
        return this.x == coord.x && this.y == coord.y
    }
}