package com.example.monopoli

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.monopoli.view.HomeScreen
import com.example.monopoli.view.LoginScreen
import com.example.monopoli.ui.theme.MonopoliTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopoliTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var isLogged by remember {mutableStateOf(auth.currentUser != null)}
                    if (!isLogged) {
                        LoginScreen(loginClick = { email, password ->
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        isLogged = true
                                        Toast.makeText(
                                            baseContext,
                                            "Authentication ok.",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            baseContext,
                                            "Authentication failed.",
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }

                        })
                    }else{
                        HomeScreen(
                            onLogout = {
                                isLogged = false // 🔥 VUELVE AL LOGIN
                            }
                        )
                        Toast.makeText(
                            baseContext,
                            "LogOut OK.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }


                    Greeting(
                        name = "",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = " $name!",
        modifier = modifier
    )

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MonopoliTheme {
        Greeting("Android")
    }
}