package com.raees.steganographicapp

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewImageScreen(
    imageId: Int = -1,
    db: StegImageDatabase
) {
    val viewModel = viewModel<ViewImageScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ViewImageScreenViewModel(db.dao) as T
            }
        }
    )

    viewModel.loadStegImage(imageId)
    val stegImage = viewModel.stegImage

    val context = LocalContext.current
    var secretKey by rememberSaveable {
        mutableStateOf("")
    }
    var messageVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .scrollable(scrollState, Orientation.Vertical)
    ) {
        Box(
            Modifier
                .fillMaxWidth(0.7f)
                .height(300.dp)
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            if (stegImage != null) {
                Image(
                    painter =  rememberAsyncImagePainter(stegImage.imageUri),
                    contentDescription = "android party"
                )
            }
        }
        TextField(
            value = secretKey,
            onValueChange = { secretKey = it},
            placeholder = {
                Text("Enter your secret key")
            },
            label = {
                Text(text = "Secret Key")
            },
            modifier = Modifier.padding(4.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (secretKey == stegImage?.password) {
                    messageVisible = true
                }
                else {
                    Toast.makeText(context, "Incorrect secret key!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(4.dp)
        ) {
            Text(text = "Reveal Text")
        }

        Column(
            Modifier
                .fillMaxWidth(.7f)
                .padding(4.dp)
        ) {
            Text("Message",
                modifier = Modifier.padding(vertical = 8.dp),
                fontWeight = FontWeight.SemiBold
            )
            AnimatedVisibility(
                visible = messageVisible,
                enter = fadeIn(tween(2000)),
                exit = fadeOut(tween(2000))
            ) {
                if (stegImage != null) Text(text = stegImage.message)
            }
        }
    }
}


//@Preview(showSystemUi = true)
//@Composable
//fun PreviewViewImageScreen() {
//    ViewImageScreen()
//}