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
package dk.itu.moapd.bluetoothconnection

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.bluetoothconnection.databinding.RowItemBinding

/**
 * A class to customize an adapter with a `ViewHolder` to populate a Device dataset into a
 * `RecyclerView`.
 */
class CustomAdapter(private val data: ArrayList<DeviceModel>) :
    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * A set of private constants used in this class.
     */
    companion object {
        private val TAG = CustomAdapter::class.qualifiedName
    }

    /**
     * An internal view holder class used to represent the layout that shows a single `Device`
     * instance in the `RecyclerView`.
     */
    class ViewHolder(private val binding: RowItemBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * This method binds the `ViewHolder` instance and more cleanly separate concerns between
         * the view holder and the adapter.
         *
         * @param device The current `Device` object.
         */
        fun bind(device: DeviceModel) {
            binding.textView.text = binding.root.context.getString(
                R.string.device_info, device.name, device.macAddress
            )

            if (device.isConnected)
                binding.imageCheckCircle.setImageResource(
                    R.drawable.baseline_check_circle_outline_24)
        }

    }

    /**
     * Called when the `RecyclerView` needs a new `ViewHolder` of the given type to represent an
     * item.
     *
     * This new `ViewHolder` should be constructed with a new `View` that can represent the items of
     * the given type. You can either create a new `View` manually or inflate it from an XML layout
     * file.
     *
     * The new `ViewHolder` will be used to display items of the adapter using
     * `onBindViewHolder(ViewHolder, int, List)`. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the `View` to
     * avoid unnecessary `findViewById(int)` calls.
     *
     * @param parent The `ViewGroup` into which the new `View` will be added after it is bound to an
     *      adapter position.
     * @param viewType The view type of the new `View`.
     *
     * @return A new `ViewHolder` that holds a View of the given view type.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = RowItemBinding
        .inflate(LayoutInflater.from(parent.context), parent, false)
        .let(::ViewHolder)

    /**
     * Called by the `RecyclerView` to display the data at the specified position. This method
     * should update the contents of the `itemView()` to reflect the item at the given position.
     *
     * Note that unlike `ListView`, `RecyclerView` will not call this method again if the position
     * of the item changes in the data set unless the item itself is invalidated or the new position
     * cannot be determined. For this reason, you should only use the `position` parameter while
     * acquiring the related data item inside this method and should not keep a copy of it. If you
     * need the position of an item later on (e.g., in a click listener), use
     * `getBindingAdapterPosition()` which will have the updated adapter position.
     *
     * Override `onBindViewHolder(ViewHolder, int, List)` instead if Adapter can handle efficient
     * partial bind.
     *
     * @param holder The `ViewHolder` which should be updated to represent the contents of the item
     *      at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "Populate an item at position: $position")

        // Bind the view holder with the selected `DummyModel` data.
        data[position].let(holder::bind)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount() = data.size

    /**
     * This method adds a new item to the dataset and updates the RecyclerView via a notification
     * observer.
     *
     * @param item The new item to be added to the dataset.
     */
    fun addItem(item: DeviceModel) {
        if (item.name.isEmpty())
            return

        data.add(item)
        notifyItemInserted(data.size)
    }

}
