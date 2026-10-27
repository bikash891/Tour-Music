package com.ridebeats.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlin.random.Random

private val Ink = Color(0xFF10151D)
private val Panel = Color(0xFF202A36)
private val Lime = Color(0xFFD8FF5A)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RideBeatsApp() }
    }
}

@Composable
private fun RideBeatsApp() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }
    var tourName by remember { mutableStateOf("Weekend Riders") }
    var roomCode by remember { mutableStateOf("") }
    var roomActive by remember { mutableStateOf(false) }
    var audioUrl by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Create a tour or join with a code.") }
    var leader by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }
    var loadedUrl by remember { mutableStateOf("") }

    DisposableEffect(player) { onDispose { player.release() } }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Lime, onPrimary = Ink, background = Ink,
            surface = Panel, onSurface = Color.White
        )
    ) {
        Column(
            Modifier.fillMaxSize().background(Ink).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🏍️", style = MaterialTheme.typography.headlineLarge)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("RideBeats", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                    Text("RIDE TOGETHER. VIBE TOGETHER.", style = MaterialTheme.typography.labelSmall, color = Lime)
                }
            }
            Card(colors = CardDefaults.cardColors(containerColor = Panel)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(if (roomActive) "TOUR ROOM" else "YOUR NEXT RIDE", color = Lime, style = MaterialTheme.typography.labelLarge)
                    Text(if (roomActive) roomCode else "Music for the whole crew", color = Color.White, style = MaterialTheme.typography.headlineSmall)
                    Text(status, color = Color.LightGray)
                }
            }
            if (!roomActive) {
                OutlinedTextField(
                    value = tourName, onValueChange = { tourName = it },
                    label = { Text("Tour name") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = {
                        roomCode = (1..6).map { "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"[Random.nextInt(32)] }.joinToString("")
                        leader = true; roomActive = true
                        status = "Demo room created on this phone."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("＋ Create a tour") }
                OutlinedTextField(
                    value = roomCode, onValueChange = { roomCode = it.uppercase().take(12) },
                    label = { Text("Friend's room code") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters)
                )
                OutlinedButton(
                    onClick = {
                        if (roomCode.isBlank()) status = "Enter a tour code first."
                        else { leader = false; roomActive = true; status = "Demo room opened locally. No network connection." }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Join a tour") }
            } else {
                Text("Tour: $tourName", color = Color.White, style = MaterialTheme.typography.titleLarge)
                Text(if (leader) "You are the tour leader" else "You joined as a rider", color = Lime)
                OutlinedTextField(
                    value = audioUrl, onValueChange = { audioUrl = it },
                    label = { Text("Authorized HTTPS audio URL") },
                    placeholder = { Text("https://example.com/song.mp3") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Button(
                    enabled = leader && audioUrl.startsWith("https://"),
                    onClick = {
                        try {
                            player.setMediaItem(MediaItem.fromUri(Uri.parse(audioUrl)))
                            player.prepare()
                            loadedUrl = audioUrl
                            isPlaying = false
                            status = "Audio loaded. Press Play to listen on this device."
                        } catch (e: Exception) { status = "Could not load audio URL." }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Load authorized audio") }
                Button(
                    enabled = loadedUrl.isNotBlank(),
                    onClick = {
                        if (player.isPlaying) { player.pause(); isPlaying = false }
                        else { player.play(); isPlaying = true }
                        status = if (isPlaying) "Playing locally on this phone." else "Playback paused."
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (isPlaying) "Ⅱ Pause" else "▶ Play") }
                OutlinedButton(
                    onClick = { player.stop(); roomActive = false; isPlaying = false; loadedUrl = ""; status = "Tour closed on this phone." },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Leave tour") }
                Text("Prototype note: room codes and playback are local only. Friends' phones are not synchronized yet.", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.weight(1f))
            Text("For safety, choose music and manage the app only when stopped.", color = Color.LightGray, style = MaterialTheme.typography.bodySmall)
        }
    }
}
