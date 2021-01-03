package com.example.battleship.internal

import com.example.battleship.data.Coordinate

open class BattleShipException (message: String = ""): Exception(message)

// Exception class for handling situations when a ship is already on the board
class FieldOccupiedException (problemCoord: Coordinate, message: String = ""): BattleShipException()