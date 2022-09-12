package com.bignerdranch.android.pennydrop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //connects activity_main.xml with MainActivity.kt
        setContentView(R.layout.activity_main)

        //finding containerFragment
        //NavHostFragment предоставляет область в вашем макете для автономной навигации.
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.containerFragment) as NavHostFragment

        //Каждый NavHostFragment имеет NavController, который определяет допустимую навигацию внутри узла навигации.
        // Это включает в себя граф навигации, а также состояние навигации, такое как текущее местоположение и задний
        // стек, которые будут сохранены и восстановлены вместе с самим NavHostFragment.
        this.navController = navHostFragment.navController

        //setupWithNavController() - Настраивает NavigationBarView для использования с NavController. Это вызовет
        // android.view.MenuItem.onNavDestinationSelected при выборе пункта меню.
        //Выбранный элемент в NavigationView будет автоматически обновляться при изменении пункта назначения.
        findViewById<BottomNavigationView>(R.id.bottom_nav).setupWithNavController(this.navController)
    }
}