package com.cleanerworld.ecoville.ui.add

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Typeface
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.text.style.CharacterStyle
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.cleanerworld.ecoville.R
import com.cleanerworld.ecoville.databinding.FragmentAddBinding
import com.cleanerworld.ecoville.ui.authentication.register.RegisterActivity.Companion.TAG
import com.cleanworld.ecoville.adapter.PlacesAutoCompleteAdapter
import com.cleanworld.ecoville.adapter.PostImagesAdapter
import com.cleanworld.ecoville.data.DataSource.places
import com.cleanworld.ecoville.data.DataSource.tabLabels
import com.cleanworld.ecoville.model.PlaceAutoComplete
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import java.util.*


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mAutoCompleteAdapter: PlacesAutoCompleteAdapter
    private lateinit var placesClient: PlacesClient

    private lateinit var STYLE_BOLD: CharacterStyle
    private lateinit var STYLE_NORMAL: CharacterStyle


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val addViewModel:AddViewModel by activityViewModels()
    private lateinit var postImagesAdapter: PostImagesAdapter


    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private var mLocationPermissionGranted: Boolean=false
    private var mLastKnownLocation: Location? = null

    private val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        addViewModel.postImages.observe(
            viewLifecycleOwner,
            Observer {
                postImages->
                postImagesAdapter.postImages=postImages
                postImagesAdapter.notifyDataSetChanged()
                setUpIndicators()
                setCurrentIndicator(0)

            }
        )


        //Contruct a Places Client
        Places.initialize(activity!!.applicationContext, getString(R.string.google_maps_key))
        placesClient=Places.createClient(activity!!)


        STYLE_BOLD = StyleSpan(Typeface.BOLD)
        STYLE_NORMAL = StyleSpan(Typeface.NORMAL)


        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val wasteTypesAdapter= context?.let { ArrayAdapter(it,R.layout.drop_down_layout, tabLabels) }
        binding.wasteTypeDropDown.setAdapter(wasteTypesAdapter)
        postImagesAdapter=PostImagesAdapter(null)
        binding.wasteImagesRecyclerview.adapter=postImagesAdapter
        binding.wasteImagesRecyclerview.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )


        (binding.wasteImagesRecyclerview.getChildAt(0) as RecyclerView).overScrollMode= RecyclerView.OVER_SCROLL_NEVER


        binding.addAPhoto.setOnClickListener {
            dispatchTakePictureIntent()
        }

        //binding.wasteLocationInputField.addTextChangedListener(filterTextWatcher)

        //mAutoCompleteAdapter = PlacesAutoCompleteAdapter(null)
       // binding.placesAutoCompleteRecyclerview.layoutManager=LinearLayoutManager(context)
       // binding.placesAutoCompleteRecyclerview.adapter=mAutoCompleteAdapter
        //mAutoCompleteAdapter!!.notifyDataSetChanged();


        //Construct a Fused Location Provider Client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)

        getLocationPermission()
        getDeviceLocation()




        return root
    }

    public fun getLocationPermission(){
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
        */
        if (ContextCompat.checkSelfPermission(
                activity!!.applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        mLocationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }


    private val filterTextWatcher: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            if (s.toString() != "") {
                val token = AutocompleteSessionToken.newInstance()

                // Use the builder to create a FindAutocompletePredictionsRequest.
                val request =
                    FindAutocompletePredictionsRequest.builder()
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build()
                placesClient.findAutocompletePredictions(request)
                    .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                        for (prediction in response.autocompletePredictions) {
                            places.add(PlaceAutoComplete(prediction.placeId,prediction.getPrimaryText(STYLE_BOLD),prediction.getFullText(STYLE_NORMAL)))
                        }
                        mAutoCompleteAdapter.mResultList= places
                    }.addOnFailureListener { exception: Exception? ->
                        if (exception is ApiException) {
                            Toast.makeText(context,"Place not found:${exception.statusCode}",Toast.LENGTH_LONG).show()
                        }
                    }




                if (binding.placesAutoCompleteRecyclerview.getVisibility() === View.GONE) {
                    binding.placesAutoCompleteRecyclerview.setVisibility(View.VISIBLE)
                }
            } else {
                if (binding.placesAutoCompleteRecyclerview.getVisibility() === View.VISIBLE) {
                    binding.placesAutoCompleteRecyclerview.setVisibility(View.GONE)
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            addViewModel.addImage(imageBitmap)

        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }


    private fun setUpIndicators(){
        val indicators= arrayOfNulls<ImageView>(postImagesAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams=
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,0)
        for(i in indicators.indices){
            indicators[i]= ImageView(activity?.applicationContext )
            indicators[i]?.let {
                it.setImageDrawable(
                    activity?.let { activity ->
                        ContextCompat.getDrawable(
                            activity.applicationContext,
                            R.drawable.circular_shape_grey
                        )
                    }
                )
                it.layoutParams=layoutParams
                binding.wasteImagesIndicatorContainer.addView(it)
            }
        }

    }

    private fun setCurrentIndicator(position: Int){
        val childCount=binding.wasteImagesIndicatorContainer.childCount

        for(i in 0 until childCount){
            val imageView=binding.wasteImagesIndicatorContainer.getChildAt(i) as ImageView
            if(i==position)
                imageView.setImageDrawable(
                    activity?.let {
                        ContextCompat.getDrawable(
                            it.applicationContext,
                            R.drawable.circular_shape_black)
                    }
                )
            else
                imageView.setImageDrawable(
                    activity?.let {
                        ContextCompat.getDrawable(
                            it.applicationContext,
                            R.drawable.circular_shape_grey)
                    }
                )

        }

    }

    @SuppressLint("MissingPermission", "LongLogTag")
    private fun getDeviceLocation() {
        /*
    * Get the best and most recent location of the device, which may be null in rare
    * cases when a location is not available.
    */

            if (mLocationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation

                locationResult.addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        mLastKnownLocation = task.result
                        var geocoder=Geocoder(context, Locale.ENGLISH)
                        try {
                            val addresses: List<Address> = geocoder.getFromLocation(
                                mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude,
                                1
                            )
                            if (addresses != null) {
                                var returnedAddress: Address = addresses.get(0)
                                var strReturnedAddress = StringBuilder("")
                                for(i in 0..returnedAddress.maxAddressLineIndex)
                                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(", ")
                                binding.wasteLocationText.text=strReturnedAddress.toString()

                            }
                            else
                                binding.wasteLocationText.text="No location found"
                        }
                        catch (e:Exception){

                            e.printStackTrace()

                        }

                    }
                }

        }

    }
}