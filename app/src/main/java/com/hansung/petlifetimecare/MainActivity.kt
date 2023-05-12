package com.hansung.petlifetimecare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hansung.petlifetimecare.community.CommunityFragment
import com.hansung.petlifetimecare.adoptPackage.AdoptHomeFragment
import com.hansung.petlifetimecare.searchPackage.SearchFragment
import com.hansung.petlifetimecare.settingPackage.SettingFragment

// Import other fragments you have

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.mainFrame, HomeFragment::class.java, null)
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.mainFrame, HomeFragment::class.java, null)
                    }
                }
                R.id.menu_search -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.mainFrame, SearchFragment::class.java, null) // Replace with actual SearchFragment
                    }
                }
                R.id.menu_community -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.mainFrame, CommunityFragment::class.java, null)
                    }
                }
//                R.id.menu_notifications -> {
//                    supportFragmentManager.commit {
//                        setReorderingAllowed(true)
//                        replace(R.id.mainFrame, NotificationsFragment::class.java, null) // Replace with actual NotificationsFragment
//                    }
//                }
                R.id.menu_settings -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.mainFrame, SettingFragment::class.java, null) // Replace with actual SettingsFragment
                    }
                }
            }
            true
        }
    }
}
