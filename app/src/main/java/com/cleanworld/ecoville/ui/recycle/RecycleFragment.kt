package com.cleanerworld.ecoville.ui.recycle

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.FragmentRecycleBinding
import com.cleanerworld.ecoville.ui.add.AddViewModel
import com.cleanworld.ecoville.adapter.WasteItemsAdapter
import com.cleanworld.ecoville.data.DataSource.tabCount
import com.cleanworld.ecoville.data.DataSource.tabIcons
import com.cleanworld.ecoville.data.DataSource.tabLabels
import com.cleanworld.ecoville.ui.MarginItemDecoration
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout

class RecycleFragment : Fragment() {

    private var _binding: FragmentRecycleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val wasteImagesViewModel: AddViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recycleViewModel =
            ViewModelProvider(this).get(RecycleViewModel::class.java)

        _binding = FragmentRecycleBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val toolBar:MaterialToolbar=root.findViewById(R.id.toolBar)
        toolBar.inflateMenu(R.menu.options_menu)

        val tabLayout: TabLayout =root.findViewById(R.id.tab_layout)


        for (i in 0 until tabCount) {
            val tab: TabLayout.Tab=tabLayout.newTab()
            tab.customView = getTabView(i)
            tabLayout.addTab(tab)
        }

        displayWasteItems()



        

        return root
    }

    private fun displayWasteItems() {
        val wasteImages=wasteImagesViewModel.postImages.value
        binding.wasteItemRecyclerView.adapter=WasteItemsAdapter(wasteImages)
        binding.wasteItemRecyclerView.addItemDecoration(
            MarginItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.item_decoration_margin),
            )
        )

    }

    private fun getTabView(i: Int): View? {
        val view: View = LayoutInflater.from(context).inflate(R.layout.tab_item, null)
        val tabText: TextView = view.findViewById(R.id.tab_text)
        val tabImage: ImageView = view.findViewById(R.id.tab_image)
        tabText.text = tabLabels[i]
        tabImage.setImageResource(tabIcons[i])
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}