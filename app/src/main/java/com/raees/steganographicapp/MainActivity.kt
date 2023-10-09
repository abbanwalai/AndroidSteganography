package com.raees.steganographicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.raees.steganographicapp.ui.theme.SteganographicAppTheme


data class BottomNavigationItem(
  val title: String,
  val route: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {



  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      SteganographicAppTheme {
        // A surface container using the 'background' color from the theme
        val items = listOf(
          BottomNavigationItem(
            title = "Home",
            route = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
          ),
//          BottomNavigationItem(
//            title = "Import",
//            route = "Import",
//            selectedIcon = Icons.Filled.Add,
//            unselectedIcon = Icons.Outlined.Add
//          ),
          BottomNavigationItem(
            title = "Embed Text",
            route = "Embed",
            selectedIcon = Icons.Filled.Edit,
            unselectedIcon = Icons.Outlined.Edit
          )
        )
        var selectedItemIndex by rememberSaveable {
          mutableIntStateOf(0)
        }
        val navController = rememberNavController()


        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

          Scaffold(
            modifier = Modifier
              .fillMaxSize()
              .nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {

              CenterAlignedTopAppBar(
                title = {
                  Text(text = "Steganographic App")
                },
                scrollBehavior = scrollBehavior
              )
            },
            bottomBar = {
              NavigationBar {
                items.forEachIndexed { index, bottomNavigationItem ->
                  NavigationBarItem(
                    label = {Text(text = bottomNavigationItem.title)},
                    selected = selectedItemIndex == index,
                    onClick = {
                      selectedItemIndex = index
                      navController.navigate(bottomNavigationItem.route)
                    },
                    icon = {
                      if (selectedItemIndex == index ) {
                        Icon(
                          imageVector = bottomNavigationItem.selectedIcon,
                          contentDescription = bottomNavigationItem.title
                        )}
                      else {
                        Icon(
                          imageVector = bottomNavigationItem.unselectedIcon,
                          contentDescription = bottomNavigationItem.title
                        )
                      }
                    }
                  )
                }
              }
            }

          ) {values ->
            Box(
              Modifier
                .fillMaxSize()
                .padding(values)
            ) {
              MyNavigation(navController)
            }
          }
        }
      }
    }
  }

}


@Composable
fun MyNavigation(navController: NavHostController) {
    val context = LocalContext.current
    val db by lazy {
        Room.databaseBuilder(
            context,
            StegImageDatabase::class.java,
            "stegimages.db"
        ).allowMainThreadQueries().build()
    }
  NavHost(navController = navController, startDestination = "Home") {
    composable(Home.route) {
      HomeScreen(navController, db)
    }
    composable(Import.route) {
      ImportScreen(navController)
    }
    composable(EmbedText.route) {
      EmbedTextScreen(navController, db)
    }
    composable(
      ViewImage.route + "/{${ViewImage.argImageId}}",
      arguments = listOf(navArgument(ViewImage.argImageId) {type = NavType.IntType})

    ) {
      val imageId= requireNotNull(it.arguments?.getInt(ViewImage.argImageId)) {"Image id is null"}
      ViewImageScreen(imageId, db)
    }
  }
}

