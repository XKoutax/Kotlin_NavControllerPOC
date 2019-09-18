package com.example.android.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.android.navigation.databinding.FragmentFinalBinding


class FinalFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return super.onCreateView(inflater, container, savedInstanceState)

        Log.d("xxx", "onCreate FinalFragment")
        val binding: FragmentFinalBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_final, container, false)

        binding.someButton.setOnClickListener { view: View ->
            view.findNavController().navigate(FinalFragmentDirections.actionFinalFragmentToGameFragment())
        }

        return binding.root

    }


    override fun onDestroyView() {
        Log.d("xxx", "onDestroy FinalFragment\n")
        super.onDestroyView()
    }


}