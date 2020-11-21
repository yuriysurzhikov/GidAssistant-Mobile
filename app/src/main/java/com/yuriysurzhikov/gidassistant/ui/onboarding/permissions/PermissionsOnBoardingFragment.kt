package com.yuriysurzhikov.gidassistant.ui.onboarding.permissions

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.yuriysurzhikov.gidassistant.R
import com.yuriysurzhikov.gidassistant.ui.onboarding.OnBoardingFragment
import com.yuriysurzhikov.gidassistant.databinding.FragmentOnboardingPermissionsBinding
import com.yuriysurzhikov.gidassistant.ui.onboarding.OnBoardingActivity
import com.yuriysurzhikov.gidassistant.utils.permissions.IPermissionsCallback
import com.yuriysurzhikov.gidassistant.utils.permissions.IPermissionsProvider
import com.yuriysurzhikov.gidassistant.utils.permissions.PermissionsProvider
import com.yuriysurzhikov.gidassistant.utils.permissions.PermissionsType
import com.yuriysurzhikov.gidassistant.utils.permissions.PermissionsType.LocationPermissions
import java.security.Permission

class PermissionsOnBoardingFragment:
    OnBoardingFragment(), IPermissionsProvider<LocationPermissions>{

    private lateinit var binding: FragmentOnboardingPermissionsBinding
    private val viewModel: PermissionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingPermissionsBinding.inflate(inflater, container, false)
        binding.image = resources.getDrawable(R.drawable.on_boarding_2)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.permssionsImage.setOnClickListener {
            requestPermissions()
        }
    }

    override fun refresh() {
        viewModel.refresh()
    }

    private val permissionsResultCallback = object: IPermissionsCallback<LocationPermissions> {
        override fun onGranted(type: LocationPermissions) {
            LocationPermissions.showGrantedMessage()
            (activity as OnBoardingActivity).onBoardingCallback.onFinishClick()
        }

        override fun onDecline(type: LocationPermissions) {
            LocationPermissions.showDeclinedMessage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            LocationPermissions.requestCode -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsResultCallback.onGranted(LocationPermissions)
                } else {
                    permissionsResultCallback.onDecline(LocationPermissions)
                }
            }
        }
    }

    override fun requestPermissions() {
        if(ContextCompat.checkSelfPermission(context!!, LocationPermissions.permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            permissionsResultCallback.onGranted(LocationPermissions)
        } else {
            requestPermissions(LocationPermissions.permissions, LocationPermissions.requestCode)
        }
    }
}