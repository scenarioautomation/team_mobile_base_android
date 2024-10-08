package com.scenarioautomation.embrace.team_mobile_base_android.features.project.add

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.scenarioautomation.embrace.team_mobile_base_android.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectFragment : Fragment() {

    companion object {
        fun newInstance() = AddProjectFragment()
    }

    private val viewModel: AddProjectViewModel by viewModels()

    private var pickPhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>? = null

    private var currentPhotoUri: Uri? = null
    private var ivPhotoPreview: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                uri?.let {
                    currentPhotoUri = uri
                    ivPhotoPreview?.let {
                        Glide.with(this)
                            .load(uri)
                            .into(it)
                    }
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ivPhotoPreview = view.findViewById(R.id.ivPhotoPreview)
        val etName = view.findViewById<EditText>(R.id.etName)

        view.findViewById<Button>(R.id.btnSelectPhoto).setOnClickListener {
            pickPhotoLauncher?.launch(
                PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly).build()
            )
        }

        val btnSave = view.findViewById<FloatingActionButton>(R.id.btnSave).apply {
            setOnClickListener {
                viewModel.addProject(etName.text.toString(), currentPhotoUri)
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                AddProjectState.INVALID_PARAMS, AddProjectState.ERROR -> {
                    btnSave.isEnabled = true
                    Snackbar.make(
                        btnSave,
                        getString(if (it == AddProjectState.INVALID_PARAMS) R.string.invalid_parameter else R.string.record_fail),
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                AddProjectState.SUCCESS -> {
                    Snackbar.make(
                        btnSave,
                        getString(R.string.record_success),
                        Snackbar.LENGTH_LONG
                    ).show()
                    parentFragmentManager.popBackStack()
                }

                AddProjectState.SALVING -> {
                    btnSave.isEnabled = false
                }

                else -> {

                }
            }
        }
    }
}