package com.example.spendwise

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendwise.ui.theme.SpendWiseTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val handler = Handler(Looper.getMainLooper())
    private val logoutRunnable = Runnable {
        logoutUser()
    }

    private val timeoutDuration: Long = 2 * 60 * 1000 // 5 minutes (in milliseconds)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SpendWiseTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SplashScreen()
                }
            }
        }

        // Navigate to LoginActivity after 3 seconds
        MainScope().launch {
            delay(3000)
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun resetLogoutTimer() {
        handler.removeCallbacks(logoutRunnable)
        handler.postDelayed(logoutRunnable, timeoutDuration)
    }

    private fun logoutUser() {
        // Redirect to login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        resetLogoutTimer() // Reset timer on any interaction
    }

    override fun onResume() {
        super.onResume()
        resetLogoutTimer()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(logoutRunnable) // Stop timer when app is paused
    }
}


@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1565C0), // Deep Blue
                        Color(0xFFFFC107)  // Vivid Gold
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        AppLogo()
    }
}

@Composable
fun AppLogo() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //  Budget Icon from drawable
        Icon(
            painter = painterResource(id = R.drawable.baseline_account_balance_wallet_24),
            contentDescription = "Budget Icon",
            tint = Color.White,
            modifier = Modifier
                .size(72.dp)
                .padding(bottom = 12.dp)
        )

        //  App Title
        Text(
            text = "SpendWise",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 2.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SpendWiseTheme {
        SplashScreen()
    }
}
