package com.example.monopoli.repositories

import com.example.monopoli.models.Room
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RoomRepository {

    private val db = FirebaseDatabase.getInstance().reference // Da acceso a la raiz de firebase, obtiene la base de datos
    //Evista multiples listeners activos
    private var roomListener: ValueEventListener? = null //Guarda el listenet activo
    private var currentRoomRef: DatabaseReference? = null // Saber a que nodo(sala) estamos escuchando


    fun createRoom(roomCode: String, userId: String, userName: String) {
        val room = mapOf(
            "host" to userName,
            "status" to "waiting",
            "players" to mapOf(userId to userName)
        )
        //Lo guardamos en el nodo rooms, y entra o crea un roomcode rooms/123456
        db.child("rooms").child(roomCode).setValue(room)
    }

    suspend fun joinRoom(roomCode: String, userId: String, userName: String): Result<Unit> {
        return try {
            //Treamos los datos desde firebase
            val snapshot = db.child("rooms").child(roomCode).get().await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Sala no existe"))
            }

            //Traemos los datos de jugadores
            val players = snapshot.child("players").children

            //Validamos el limite de jugadores
            if (players.count() >= 10) {
                return Result.failure(Exception("Sala llena"))
            }

            //Agrega el jugador a la sala
            db.child("rooms").child(roomCode).child("players").child(userId).setValue(userName)

            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun listenRoom(roomCode: String, onChange: (Room) -> Unit) {

        stopListener() //Evitamos duplicados, elimindando la sala que ya fue jugada o cancelada o de otra persona.

        val ref= db.child("rooms").child(roomCode) // Obtenemos la sala
        currentRoomRef = ref

        var listener = object : ValueEventListener {
            //Cuando cambiamos datos
            override fun onDataChange(snapshot: DataSnapshot) {
                    //Convvertimos el snapshot a objeto
                    val room = snapshot.getValue(Room::class.java)
                    if(room != null)onChange(room)
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        roomListener = listener //Guardamos el listener
        ref.addValueEventListener(listener) //Activamos ese evento a "escuchar cambios"
    }

    fun stopListener(){
        //Verifica si existe
        roomListener?.let {listener -> //Solo entramos si roomListener no es null
            currentRoomRef?.removeEventListener(listener) // Le decimos a firebase que deje de enviar actualizacones
        }
        //Borramos referencias, pra liberar memoria y evitar errores
        roomListener = null
        currentRoomRef = null
    }

    //Cambiamos estados en este caso Playing
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