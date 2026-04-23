package com.example.monopoli.repositories

import com.example.monopoli.models.GameState
import com.example.monopoli.models.Player
import com.example.monopoli.models.Room
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RoomRepository {

    private val db = FirebaseDatabase.getInstance().reference

    fun createRoom(roomCode: String, userId: String, userName: String) {

        val room = mapOf(
            "host" to userName,
            "status" to "waiting",
            "players" to mapOf(userId to userName)
        )

        db.child("rooms").child(roomCode).setValue(room)
    }

    suspend fun joinRoom(
        roomCode: String,
        userId: String,
        userName: String
    ): Result<Unit> {
        return try {
            val snapshot = db.child("rooms").child(roomCode).get().await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Sala no existe"))
            }

            val players = snapshot.child("players").children

            if (players.count() >= 10) {
                return Result.failure(Exception("Sala llena"))
            }

            db.child("rooms")
                .child(roomCode)
                .child("players")
                .child(userId)
                .setValue(userName)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun listenGameState(roomCode: String, onChange: (GameState) -> Unit) {
        db.child("rooms").child(roomCode).child("gameState")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val playerNum = snapshot.child("playerNum").getValue(Int::class.java) ?: 0
                    val isFinished = snapshot.child("isFinished").getValue(Boolean::class.java) ?: false
                    val turns = snapshot.child("turns").children.map {
                        it.getValue(Int::class.java) ?: 0
                    }
                    val players = snapshot.child("players").children.mapNotNull { playerSnap ->
                        val id = playerSnap.key ?: return@mapNotNull null
                        val name = playerSnap.child("name").getValue(String::class.java) ?: ""
                        val money = playerSnap.child("money").getValue(Float::class.java) ?: 1000f
                        val playing = playerSnap.child("playing").getValue(Boolean::class.java) ?: true
                        Player(id = id, name = name, money = money, playing = playing)
                    }
                    if (players.isNotEmpty()) {
                        onChange(GameState(players, playerNum, turns, isFinished = isFinished))
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun updateGameState(roomCode: String, newState: GameState) {
        val data = mapOf(
            "playerNum" to newState.playerNum,
            "turns" to newState.turns,
            "isFinished" to newState.isFinished,
            "players" to newState.players.associate { player ->
                player.id to mapOf(
                    "name" to player.name,
                    "money" to player.money,
                    "playing" to player.playing
                )
            }
        )
        db.child("rooms").child(roomCode).child("gameState").updateChildren(data)
    }

    fun listenRoom(roomCode: String, onChange: (Room) -> Unit) {

        db.child("rooms").child(roomCode)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val room = snapshot.getValue(Room::class.java)
                    /* Si el host sale (sugerencia de como manejarlo)
                    val hostId = snapshot.child("host_id").getValue(String::class.java)
                    val players = snapshot.child("players").children.mapNotNull {
                        it.key
                    }

                    if (hostId != null && !players.contains(hostId)) {
                        endGame(roomCode)
                    }
                    */
                    if (room != null) {
                        onChange(room)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    fun startGame(roomCode: String) {
        db.child("rooms").child(roomCode).child("status").setValue("playing")
    }

    //Usamos funcion para eliminar
    fun removePlayer(roomCode: String, userId: String) {
        db.child("rooms").child(roomCode).child("players").child(userId).removeValue()
    }
    //Usamos funcion removeValue para eliminar la sala
    fun deleteRoom(roomCode: String) {
        db.child("rooms").child(roomCode).removeValue()
    }

}