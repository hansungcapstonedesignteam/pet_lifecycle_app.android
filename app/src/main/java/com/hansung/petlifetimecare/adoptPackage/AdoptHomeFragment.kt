package com.hansung.petlifetimecare.adoptPackage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.hansung.petlifetimecare.databinding.ActivityAdopt1Binding

class AdoptHomeFragment : Fragment() {
    private var _binding: ActivityAdopt1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityAdopt1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Define the fragments for the ViewPager2
        val fragments = listOf(
            GetFragment(),
            GiveFragment(),
            HelpFragment()
        )

        // Create the adapter
        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, fragments)
        binding.viewPager.adapter = adapter

        // Connect the TabLayout and ViewPager2
        TabLayoutMediator(binding.tabs, binding.viewPager) { ta, position ->
            ta.text = when (position) {
                0 -> "입양 받기"
                1 -> "입양시키기"
                2 -> "HELP"
                else -> throw IndexOutOfBoundsException()
            }
        }.attach()
    }

}

class ViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int) = fragments[position]
}
