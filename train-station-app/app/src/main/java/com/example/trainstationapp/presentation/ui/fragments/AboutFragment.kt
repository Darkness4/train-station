package com.example.trainstationapp.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trainstationapp.R
import com.example.trainstationapp.databinding.FragmentAboutBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.licensesButton.setOnClickListener { openLicences() }
        binding.sourceCodeButton.setOnClickListener { openSourceCode() }
        return binding.root
    }

    private fun openSourceCode() {
        val uri: Uri = Uri.parse(getString(R.string.project_url))
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

    private fun openLicences() {
        startActivity(Intent(context, OssLicensesMenuActivity::class.java))
    }

    companion object {
        @JvmStatic
        fun newInstance() = AboutFragment()
    }
}
