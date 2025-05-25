package com.grafitto.songsapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.grafitto.songsapp.ui.viewmodel.CategoryViewModel

data class NavItemData(
    val route: String,
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
)

@Composable
fun getDynamicNavItems(
    navController: NavHostController,
    currentRoute: String?,
    baseNavItems: List<Triple<String, ImageVector, String>>,
    categoryViewModel: CategoryViewModel?,
): List<NavItemData> {
    val defaultNavOnClick: (String) -> () -> Unit = { route ->
        {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                launchSingleTop = true
            }
        }
    }

    val isCategoriesSectionActive =
        currentRoute != null &&
            (currentRoute == "categories" || currentRoute.startsWith("categories/") || currentRoute.startsWith("category_edit"))

    val categoriesNavItem =
        NavItemData(
            route = if (isCategoriesSectionActive) currentRoute!! else "categories",
            icon = Icons.AutoMirrored.Filled.List,
            label = "Categorías",
            onClick = defaultNavOnClick("categories"),
        )

    return when {
        currentRoute == "categories" ||
            (
                currentRoute?.startsWith("categories/") == true &&
                    currentRoute != "category_edit" &&
                    !currentRoute.startsWith("category_edit/")
            ) -> {
            val items = mutableListOf<NavItemData>()
            items.add(NavItemData("main", Icons.Default.Home, "Inicio", defaultNavOnClick("main")))
            items.add(categoriesNavItem)
            items.add(
                NavItemData("category_create", Icons.Default.Add, "Crear") {
                    val parentIdArg =
                        if (currentRoute.startsWith("categories/")) {
                            navController.currentBackStackEntry?.arguments?.getString("parentId")
                        } else {
                            null
                        }
                    if (parentIdArg != null) {
                        navController.navigate("category_edit?parentId=$parentIdArg")
                    } else {
                        navController.navigate("category_edit")
                    }
                },
            )
            if (currentRoute.startsWith("categories/") && navController.previousBackStackEntry != null) {
                items.add(NavItemData("back", Icons.AutoMirrored.Filled.ArrowBack, "Atrás") { navController.popBackStack() })
            }
            items
        }
        currentRoute?.startsWith("category_edit") == true -> {
            listOfNotNull(
                NavItemData("main", Icons.Default.Home, "Inicio", defaultNavOnClick("main")),
                categoriesNavItem,
                categoryViewModel?.let { vm -> NavItemData("category_save", Icons.Filled.CloudUpload, "Guardar") { vm.requestSave() } },
                NavItemData("back", Icons.AutoMirrored.Filled.ArrowBack, "Atrás") { navController.popBackStack() },
            )
        }
        else -> {
            baseNavItems.map { (baseRoute, icon, label) ->
                if (baseRoute == "categories") {
                    categoriesNavItem
                } else {
                    NavItemData(baseRoute, icon, label, defaultNavOnClick(baseRoute))
                }
            }
        }
    }
}
