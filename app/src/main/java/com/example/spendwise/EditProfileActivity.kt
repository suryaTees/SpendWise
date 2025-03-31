package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.theme.SpendWiseTheme
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                EditProfileScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
    val user = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current

    val name = user?.displayName ?: "Surya"
    val email = user?.email ?: "Not available"
    val location = "Hyderabad, India" // Optional static location for now

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text("Profile", fontWeight = FontWeight.Bold)
            })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1565C0), Color(0xFFFFC107))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "Your Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileField("Name", name)
                    ProfileField("Email", email)
                    ProfileField("Location", location)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Sign Out")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("$label:", fontWeight = FontWeight.SemiBold)
        Text(value, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))
    }
}
