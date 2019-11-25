package com.example.homes.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.br.homes.R
import com.br.homes.Util.myhomes
import kotlinx.android.synthetic.main.fragment_help.*

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_help, container, false)
        return root
    }

    override fun onResume() {
        super.onResume()
        myhomes = false
    }
}