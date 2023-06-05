package com.hansung.petlifetimecare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.google.android.gms.common.api.internal.LifecycleCallback.getFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.hansung.petlifetimecare.community.CommunityFragment
import com.hansung.petlifetimecare.adoptPackage.AdoptHomeFragment
import com.hansung.petlifetimecare.databinding.ActivityMainBinding
import com.hansung.petlifetimecare.dogChoicePackage.StartDogFragment
import com.hansung.petlifetimecare.searchPackage.SearchFragment
import com.hansung.petlifetimecare.settingPackage.SettingFragment

// Import other fragments you have

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        val bottomNavigationView: BottomNavigationView = binding.bottomNavigationView
        setContentView(view)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    val fragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
                    true
                }
                R.id.menu_search -> {
                    val fragment = SearchFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
                    true
                }
                R.id.menu_community -> {
                    val fragment = CommunityFrontFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
                    true
                }
                R.id.menu_choice -> {
                    val fragment = StartDogFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
                    true
                }
//                R.id.menu_notifications -> {
//                    TODO("Notification Fragment 미완성")
//                    val fragment =
//                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
//                    true
//                }
//                R.id.menu_settings -> {
//                    val fragment = SettingFragment()
//                    supportFragmentManager.beginTransaction().replace(R.id.mainFrame, fragment).commit()
//                    true
//                }
                else -> false
            }
        }

        supportFragmentManager.beginTransaction().replace(R.id.mainFrame, HomeFragment()).commit()
    }
}
