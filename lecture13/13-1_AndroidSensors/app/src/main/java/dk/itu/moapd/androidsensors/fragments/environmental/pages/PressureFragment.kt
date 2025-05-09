/*
 * MIT License
 *
 * Copyright (c) 2025 Fabricio Batista Narcizo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package dk.itu.moapd.androidsensors.fragments.environmental.pages

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.itu.moapd.androidsensors.R
import dk.itu.moapd.androidsensors.databinding.FragmentSingleValueBinding

/**
 * A fragment to show the pressure data get from an Android environmental sensor.
 *
 * The `MainActivity` has a `FragmentContainerView` area to replace dynamically the fragments used
 * by this project. You can use a bundle to share data between the main activity and this fragment.
 * We have used a `ViewModel` from the `MainActivity` to execute the updates on UI components.
 */
class PressureFragment : Fragment() {

    /**
     * Used for receiving notifications from the SensorManager when there is new sensor data.
     */
    private val pressureListener: SensorEventListener = object : SensorEventListener {

        /**
         * Called when there is a new sensor event. Note that "on changed" is somewhat of a
         * misnomer, as this will also be called if we have a new reading from a sensor with the
         * exact same sensor values (but a newer timestamp).
         *
         * The application doesn't own the `android.hardware.SensorEvent` object passed as a
         * parameter and therefore cannot hold on to it. The object may be part of an internal pool
         * and may be reused by the framework.
         *
         * @param event The SensorEvent instance.
         */
        override fun onSensorChanged(event: SensorEvent) {
            binding.singleValue.text = getString(R.string.pascal_text, event.values[0])
        }

        /**
         * Called when the accuracy of the registered sensor has changed. Unlike
         * `onSensorChanged()`, this is only called when this accuracy value changes.
         *
         * @param sensor An instance of the `Sensor` class.
         * @param accuracy The new accuracy of this sensor, one of `SensorManager.SENSOR_STATUS_`
         */
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        }

    }

    /**
     * View binding is a feature that allows you to more easily write code that interacts with
     * views. Once view binding is enabled in a module, it generates a binding class for each XML
     * layout file present in that module. An instance of a binding class contains direct references
     * to all views that have an ID in the corresponding layout.
     */
    private var _binding: FragmentSingleValueBinding? = null

    /**
     * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
     */
    private val binding
        get() = requireNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    /**
     * An instance that lets you access the Android device's sensors.
     */
    private lateinit var sensorManager: SensorManager

    /**
     * Called to have the fragment instantiate its user interface view. This is optional, and
     * non-graphical fragments can return null. This will be called between `onCreate(Bundle)` and
     * `onViewCreated(View, Bundle)`. A default View can be returned by calling `Fragment(int)` in
     * your constructor. Otherwise, this method returns `null`.
     *
     * It is recommended to <strong>only</strong> inflate the layout in this method and move logic
     * that operates on the returned View to `onViewCreated(View, Bundle)`.
     *
     * If you return a View from here, you will later be called in `onDestroyView()` when the view
     * is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the
     *      fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be
     *      attached to. The fragment should not add the view itself, but this can be used to
     *      generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSingleValueBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    /**
     * Called immediately after `onCreateView(LayoutInflater, ViewGroup, Bundle)` has returned, but
     * before any saved state has been restored in to the view. This gives subclasses a chance to
     * initialize themselves once they know their view hierarchy has been completely created. The
     * fragment's view hierarchy is not however attached to its parent at this point.
     *
     * @param view The View returned by `onCreateView(LayoutInflater, ViewGroup, Bundle)`.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous
     *      saved state as given here.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get an instance of the Android Sensor Manager using functional approach.
        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    /**
     * Called when the fragment is visible to the user and actively running. This is generally tied
     * to `Activity.onResume()` of the containing Activity's lifecycle.
     */
    override fun onResume() {
        super.onResume()

        // Get an instance of the pressure sensor and register the sensor listener.
        sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)?.let { pressure ->
            sensorManager.registerListener(
                pressureListener, pressure, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Inform the users that there is no pressure sensor in their mobile devices.
            binding.singleValue.text = getString(R.string.unavailable)
        }

        // Change the image icon.
        binding.imageView.setImageResource(R.drawable.baseline_pressure_128)
    }

    /**
     * Called when the Fragment is no longer resumed. This is generally tied to `Activity.onPause()`
     * of the containing Activity's lifecycle.
     */
    override fun onPause() {
        super.onPause()

        // When the Fragment is not visible, unregister the pressure listener.
        sensorManager.unregisterListener(pressureListener)
    }

    /**
     * Called when the view previously created by `onCreateView()` has been detached from the
     * fragment. The next time the fragment needs to be displayed, a new view will be created. This
     * is called after `onStop()` and before `onDestroy()`. It is called <em>regardless</em> of
     * whether `onCreateView()` returned a non-null view. Internally it is called after the view's
     * state has been saved but before it has been removed from its parent.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
