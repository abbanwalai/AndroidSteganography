package com.raees.steganographicapp

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController? = null,
    db: StegImageDatabase
) {
    val viewModel = viewModel<HomeScreenViewModel>(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeScreenViewModel(db.dao) as T
            }
        }
    )

    val context = LocalContext.current
    viewModel.getPhotos(context)
    val photos = viewModel.photos


    if (photos.isNotEmpty()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize(),

        ) {
            items(photos.size) {
                Card(
                    onClick = {
                        navController?.navigate(ViewImage.route + "/${photos[it].id}")
                    },
                    modifier = Modifier
                        .height(100.dp)
                        .padding(2.dp),

                ) {
                    Image(
                        painter = rememberAsyncImagePainter(model = Uri.parse(photos[it].imageUri)),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    else {
       Column(
           Modifier.fillMaxSize(),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           Button(
               onClick = {
                   viewModel.getPhotos(context)
               }
           ) {
               Text(text = "Show photos")
           }
        }
    }
}

//
//@Preview(showSystemUi = true)
//@Composable
//fun PreviewHomeScreen() {
//    HomeScreen()
//}