package com.example.battleship.ui.setup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.battleship.R
import com.example.battleship.data.Board
import com.example.battleship.data.Player
import kotlinx.android.synthetic.main.activity_setup.*
import java.lang.StrictMath.floor

class SetupActivity : AppCompatActivity() {
    private lateinit var board: Board
    private lateinit var boardAdapter: BoardGridAdapter

    private val player = Player("Dmitry", 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)

        initBoard()

        randomButton.setOnClickListener {
            generateRandomShips()
        }
    }

    private fun initBoard() {
        board = Board()

        boardAdapter = BoardGridAdapter(this, board.getFieldStatus())
        { view: View, position: Int -> handleBoardClick(view, position) }

        boardGridView.adapter = boardAdapter
    }

    private fun handleBoardClick(view: View, position: Int) {

        val x: Int = floor((position / board.boardX).toDouble()).toInt()
        val y: Int = position % board.boardX

    }

    private fun generateRandomShips() {
        board = Board()
        player.generateShips(board)
        boardAdapter.refresh(board.getFieldStatus())
    }
}