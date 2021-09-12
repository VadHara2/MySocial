package com.vadhara7.mysocialnetwork.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.vadhara7.mysocialnetwork.R
import com.vadhara7.mysocialnetwork.databinding.ActivityMainBinding
import com.vadhara7.mysocialnetwork.ui.auth.AuthActivity
import com.vadhara7.mysocialnetwork.ui.main.fragments.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var lastFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpBottomNavigationView()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miLogout -> {
                FirebaseAuth.getInstance().signOut()
                Intent(this, AuthActivity::class.java).also {
                    startActivity(it)
                }
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun makeCurrentFragment(fragment: Fragment) =
        this.supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }

    private fun setUpBottomNavigationView() {
        val homeFragment = HomeFragment()
        val searchFragment = SearchFragment()
        val createPostFragment = CreatePostFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()
        lastFragment = homeFragment
        makeCurrentFragment(homeFragment)

        binding.apply {

            bottomNavigationView.apply {
                background = null
                menu.getItem(2).isEnabled = false

                setOnItemSelectedListener {
                    when (it.itemId) {

                        R.id.homeFragment -> {
                            makeCurrentFragment(homeFragment)
                            true
                        }

                        R.id.searchFragment -> {
                            makeCurrentFragment(searchFragment)
                            true
                        }

                        R.id.profileFragment -> {
                            makeCurrentFragment(profileFragment)
                            true
                        }

                        R.id.settingsFragment -> {
                            makeCurrentFragment(settingsFragment)
                            true
                        }

                        else -> false
                    }
                }
            }

            fabNewPost.setOnClickListener {
                makeCurrentFragment(createPostFragment)
            }
        }
    }
}