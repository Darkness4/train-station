package com.example.trainstationapp.presentation.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.trainstationapp.R
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

class AboutFragment : Fragment() {
    private lateinit var sourceCodeButton: Button
    private lateinit var licensesButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        sourceCodeButton = view.findViewById(R.id.source_code_button)
        licensesButton = view.findViewById(R.id.licenses_button)

        sourceCodeButton.setOnClickListener { openSourceCode() }
        licensesButton.setOnClickListener { openLicences() }
        return view
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
