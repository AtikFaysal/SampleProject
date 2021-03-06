package com.ecommerce.practiceproject.view

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log

import android.view.ViewGroup

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.ecommerce.practiceproject.R
import com.ecommerce.practiceproject.adapter.UserListAdapter
import com.ecommerce.practiceproject.adapter.UserListAdapterPagination
import com.ecommerce.practiceproject.config.ViewModelFactory
import com.ecommerce.practiceproject.core.IPreferenceHelper
import com.ecommerce.practiceproject.core.PreferenceManager
import com.ecommerce.practiceproject.databinding.LayoutFragmentOneBinding
import com.ecommerce.practiceproject.model.UserRepository
import com.ecommerce.practiceproject.view_model.FragmentViewModel
import layout.ViewUtils

/**
 * Date 9/19/2021.
 * Created by Md Atik Faysal(Android Developer)
 *
 */
class FragmentOne : Fragment() {

    private lateinit var binding : LayoutFragmentOneBinding
    private lateinit var viewModel : FragmentViewModel
    private val adapter = UserListAdapterPagination()

    private val preferenceHelper: IPreferenceHelper by lazy { PreferenceManager(requireContext()) }

    @Nullable
    override fun onCreateView(@NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_fragment_one, container, false)
        viewModel = ViewModelProviders.of(this, ViewModelFactory(UserRepository())).get(FragmentViewModel::class.java)
        val view =  binding.root
        binding.lifecycleOwner = this
        binding.userList = viewModel

        preferenceHelper.setApiKey("Map key")
        preferenceHelper.setUserId("User Id")

        Log.d("print_api_key", preferenceHelper.getApiKey())
        Log.d("print_user_id", preferenceHelper.getUserId())

        binding.btnAddNewUser.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_fragmentOne_to_fragmentTwo)
        }

        ViewUtils.verticalRecyclerView(requireContext(), binding.rvList).adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserList()
        viewModel.getUserListFromRemote()

//        lifecycleScope.launch {
//            viewModel.listData.collect {
//                adapter.submitData(it)
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.job.cancel()
    }

    private fun getUserList()
    {
        viewModel.mlUserListRemote.observe(viewLifecycleOwner,{
            Log.d("userListSize","${it.size}")
            if(it.size > 0)
            {
                val adapter = UserListAdapter(requireContext(), it)
                ViewUtils.verticalRecyclerView(requireContext(), binding.rvList).adapter = adapter
                adapter.notifyDataSetChanged()
            }
        })
    }
}