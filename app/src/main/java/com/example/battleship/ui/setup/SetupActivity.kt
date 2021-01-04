package com.example.battleship.ui.setup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.battleship.R
import com.example.battleship.data.models.*
import com.example.battleship.internal.FieldOccupiedException
import com.example.battleship.internal.getViewModel
import com.example.battleship.ui.MainActivity.Companion.ME_PLAYER
import com.example.battleship.ui.MainActivity.Companion.ROLE_NAME
import com.example.battleship.ui.MainActivity.Companion.ROOM_NAME
import com.example.battleship.ui.MainActivity.Companion.VS_PLAYER
import com.example.battleship.ui.game.GameActivity
import kotlinx.android.synthetic.main.activity_setup.*
import java.lang.StrictMath.floor

class SetupActivity : AppCompatActivity(){

    companion object {
        const val BOARD = "BOARD"
        const val FLEET = "FLEET"
    }

    private lateinit var shipAdapter: ShipListAdapter
    private lateinit var boardAdapter: BoardGridAdapter

    private val viewModel by lazy {
        getViewModel { SetupViewModel() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        title = getString(R.string.place_ships)

        initObservers()
        initBoardAdapter()
        initShipAdapter()

        viewModel.roomName = intent.getStringExtra(ROOM_NAME) as String
        viewModel.roleName = intent.getStringExtra(ROLE_NAME) as String

        viewModel.myPlayer = intent.getParcelableExtra<Player>(ME_PLAYER) as Player
        viewModel.vsPlayer = intent.getParcelableExtra<Player>(VS_PLAYER) as Player

        randomButton.setOnClickListener {
            viewModel.generateRandomShips()
            startButton.visibility = View.VISIBLE
            viewModel.startGameVisibility = true
        }

        manualButton.setOnClickListener {
            viewModel.initShips()
            randomButton.visibility = View.GONE;
            manualButton.visibility = View.GONE;
            viewModel.shipListVisibility = true
            updateVisibility()
        }

        rotateButton.setOnClickListener {
            viewModel.rotateShip()
        }

        startButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(ROOM_NAME, viewModel.roomName)
            intent.putExtra(ROLE_NAME, viewModel.roleName)
            intent.putExtra(ME_PLAYER, viewModel.myPlayer)
            intent.putExtra(VS_PLAYER, viewModel.vsPlayer)
            intent.putExtra(BOARD, viewModel.board.fieldStatus)
            intent.putExtra(FLEET, viewModel.board.fleet)
            this.startActivity(intent)
            finish()
        }

    }

    private fun initObservers() {
        viewModel.apply {
            refreshBoardLiveData.observe(this@SetupActivity,
                Observer { board -> refreshBoard(board) })
            shipsLiveData.observe(this@SetupActivity,
                Observer { ships -> addDataToShipAdapter(ships) })
            refreshShipsLiveData.observe(this@SetupActivity,
                Observer { shipList -> refreshShips(shipList) })
        }
    }

    override fun onResume() {
        super.onResume()
        updateVisibility()
    }

    private fun updateVisibility() {
        if (!viewModel.startGameVisibility) {
            shipsLayout.visibility = if (viewModel.shipListVisibility) View.VISIBLE else View.GONE
            randomButton.visibility = if (viewModel.shipListVisibility) View.GONE else View.VISIBLE
            manualButton.visibility = if (viewModel.shipListVisibility) View.GONE else View.VISIBLE
        } else {
            startButton.visibility = View.VISIBLE
        }
    }

    private fun refreshBoard(board: Board) {
        boardAdapter.refresh(board.fieldStatus)
        randomButton.visibility = View.GONE;
        manualButton.visibility = View.GONE;
    }

    private fun initBoardAdapter() {
        boardAdapter =
            BoardGridAdapter(
                this,
                viewModel.board.fieldStatus
            )
            { view: View, position: Int -> handleBoardClick(view, position) }

        boardGridView.adapter = boardAdapter
    }


    private fun addDataToShipAdapter(ships: ArrayList<Ship>) {
        shipAdapter.refreshShipList(ships)
    }

    private fun refreshShips(shipList: ArrayList<Ship>) {
        shipAdapter.selectedPosition = -1
        shipAdapter.refreshShipList(shipList)

        if (viewModel.isShipListEmpty()) {
            rotateButton.visibility = View.GONE
            startButton.visibility = View.VISIBLE
            viewModel.startGameVisibility = true
        }
    }

    private fun initShipAdapter() {
        shipAdapter = ShipListAdapter(this)
        shipListView.adapter = shipAdapter

        shipListView.setOnItemClickListener { _, _, position, _ ->
            val selectedShip = shipAdapter.getItem(position) as Ship
            viewModel.selectedShip(selectedShip)
            shipAdapter.selectedPosition = position;
            shipAdapter.notifyDataSetChanged();
        }
    }

    private fun handleBoardClick(view: View, position: Int) {
        viewModel.handleBoardClick(view, position)
    }



}