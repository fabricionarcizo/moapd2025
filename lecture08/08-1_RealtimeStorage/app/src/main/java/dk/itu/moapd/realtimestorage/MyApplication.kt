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
package dk.itu.moapd.realtimestorage

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import io.github.cdimascio.dotenv.dotenv

/**
 * Global Kotlin extension that resolves to the short version of the name of the current class. Used
 * for labelling logs.
 */
inline fun <reified T> T.TAG(): String = T::class.java.simpleName

/**
 * Firebase Realtime Database URL.
 *
 * IMPORTANT: This is a sensitive information and should not be hardcoded in the source code. You
 * must create a `env` file in the `/app/src/main/assets` directory and add the following line:
 * DATABASE_URL=https://xxxxxxxxxx-default-rtdb.europe-west1.firebasedatabase.app
 */
val DATABASE_URL: String = dotenv {
    directory = "/assets"
    filename = "env"
}["DATABASE_URL"]

/**
 * My personalized base class for maintaining global application state. You can provide your own
 * implementation by creating a subclass and specifying the fully-qualified name of this subclass as
 * the `android:name` attribute in your `AndroidManifest.xml's` <code>&lt;application&gt;</code>
 * tag. The Application class, or your subclass of the Application class, is instantiated before any
 * other class when the process for your `application/package` is created.
 *
 * <strong>Note: </strong>There is normally no need to subclass Application. In most situations,
 * static singletons can provide the same functionality in a more modular way. If your singleton
 * needs a global context (for example to register broadcast receivers), include
 * `Context.getApplicationContext()` as a `android.content.Context` argument when invoking your
 * singleton's `getInstance()` method.
 */
class MyApplication: Application() {

    /**
     * Called when the application is starting, before any activity, service, or receiver objects
     * (excluding content providers) have been created.
     *
     * Implementations should be as quick as possible (for example using lazy initialization of
     * state) since the time spent in this function directly impacts the performance of starting the
     * first activity, service, or receiver in a process.
     *
     * If you override this method, be sure to call super.onCreate().
     *
     * Be aware that direct boot may also affect callback order on Android
     * `android.os.Build.VERSION_CODES#N` and later devices. Until the user unlocks the device, only
     * direct boot aware components are allowed to run. You should consider that all direct boot
     * unaware components, including such `android.content.ContentProvider`, are disabled until user
     * unlock happens, especially when component callback order matters.
     */
    override fun onCreate() {
        super.onCreate()

        // Apply dynamic colors to activities if available.
        DynamicColors.applyToActivitiesIfAvailable(this)

        // Enable disk persistence for the Firebase Realtime Database and keep it synchronized.
        Firebase.database(DATABASE_URL).setPersistenceEnabled(true)
        Firebase.database(DATABASE_URL).reference.keepSynced(true)
    }

}
