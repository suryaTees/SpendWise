package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpendWiseTheme {
                SettingsScreen()
            }
        }
    }
}

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    var showPrivacyPolicy by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1565C0), Color(0xFFFFC107))))
            .padding(24.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                "Settings",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Profile Section
            Text(
                text = "👤 Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${user?.email ?: "Not available"}", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    context.startActivity(Intent(context, EditProfileActivity::class.java))
                }
            ) {
                Text("Edit Profile")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Privacy Policy
            Text(
                text = "📃 Privacy Policy",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.clickable { showPrivacyPolicy = true }
            )

            if (showPrivacyPolicy) {
                AlertDialog(
                    onDismissRequest = { showPrivacyPolicy = false },
                    confirmButton = {
                        TextButton(onClick = { showPrivacyPolicy = false }) {
                            Text("Close")
                        }
                    },
                    title = { Text("Privacy Policy") },
                    text = {
                        Text("This app does not collect personal data. All your information is stored securely in Firebase.")
                    }
                )
            }
        }
    }
}
