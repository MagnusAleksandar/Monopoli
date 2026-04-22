package com.example.monopoli.repositories

import com.example.monopoli.models.GameState
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

    fun endGame(roomCode: String){
        // código que elimina la sala
    }

    fun updateGameState(roomCode: String, newState: GameState){
        // código que actualiza la sala
        /* Sugerencia de GPT:
        val gameRef = database.child("games").child(roomCode)

        val data = mapOf(
            "curr_player" to gameState.currPlayer.id,
            "round_num" to gameState.turns.maxOrNull(),
            "status" to if (gameState.isFinished) "finished" else "playing"
        )

        gameRef.updateChildren(data)
         */
    }

}