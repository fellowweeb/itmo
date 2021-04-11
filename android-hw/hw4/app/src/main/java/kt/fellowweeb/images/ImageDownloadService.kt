package kt.fellowweeb.images

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.IBinder
import android.util.Log
import android.util.LruCache
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kt.fellowweeb.images.data.UnsplashPhoto
import java.io.ByteArrayOutputStream
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.URL


class ImageDownloadService : Service() {
    companion object {
        const val COMMAND_KEY = "COMMAND"

        const val EXIT_COMMAND = 0
        const val DOWNLOAD_IMAGE_LIST = 1
        const val DOWNLOAD_IMAGE_BITMAP = 2

        const val BITMAP_KEY = "BITMAP"
        const val IMAGE_URL_KEY = "IMAGE_INFO"

        const val COUNT_KEY = "COUNT"
        const val DEFAULT_COUNT = 30

        const val PENDING_INTENT_KEY = "PENDING_INTENT"

    }

    val bitmapCache = object: LruCache<String, ByteArray>(
        (Runtime.getRuntime().maxMemory() / 1024).toInt() / 4
    ) {
        override fun sizeOf(key: String?, value: ByteArray?): Int {
            return (value?.size ?: 0) / 1024
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val exit = {super.onStartCommand(intent, flags, startId)}
        when (intent?.getIntExtra(COMMAND_KEY, EXIT_COMMAND)) {
            DOWNLOAD_IMAGE_LIST -> {
                val count = intent.getIntExtra(COUNT_KEY, DEFAULT_COUNT)
                val pendingIntent = intent.getParcelableExtra<PendingIntent>(PENDING_INTENT_KEY)
                if (pendingIntent != null) {
                    GetImageListAsyncTask(WeakReference(this), pendingIntent).execute(count)
                }
            }
            DOWNLOAD_IMAGE_BITMAP -> {
                val pendingIntent = intent.getParcelableExtra<PendingIntent>(PENDING_INTENT_KEY)
                if (pendingIntent != null) {
                    intent.getStringExtra(IMAGE_URL_KEY)?.also { url ->
                        ImageDownloadAsyncTask(
                            WeakReference(this),
                            pendingIntent,
                            url
                        ).execute()
                    }
                }
            }
        }
        return exit()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}

private class GetImageListAsyncTask(
    private val service: WeakReference<ImageDownloadService>,
    private val pendingIntent: PendingIntent
): AsyncTask<Int, Unit, List<UnsplashPhoto>>() {
    override fun doInBackground(vararg params: Int?): List<UnsplashPhoto>? {
        val count = params.first()
        if (count == null || count <= 0) {
            return emptyList()
        }
        return try {
            val input = URL("$API_URL?count=$count;client_id=$API_TOKEN").openStream()
            val turnsType = object : TypeToken<List<UnsplashPhoto>>() {}.type
            Gson().fromJson(InputStreamReader(input), turnsType)
        } catch (e: Exception) {
            Log.d("ASYNC_TASK", e.message ?: "")
            e.printStackTrace()
            null
        }
    }

    override fun onPostExecute(result: List<UnsplashPhoto>?) {
        if (result != null) {
            val intent = Intent().putExtra(IMAGES_KEY, ImageListHolder(result))
            pendingIntent.send(service.get(), 0, intent)
        }
    }
}

private class ImageDownloadAsyncTask(
    private val service: WeakReference<ImageDownloadService>,
    private val pendingIntent: PendingIntent,
    private val url: String
): AsyncTask<Unit, Unit, Bitmap?>() {

    override fun doInBackground(vararg params: Unit?): Bitmap? {
        val cache = service.get()?.bitmapCache

        var bitmap: Bitmap? = null

        val bitmapByteArray = cache?.get(url)

        if (bitmapByteArray != null) {
            bitmap = BitmapFactory.decodeByteArray(bitmapByteArray, 0, bitmapByteArray.size)
        }

        if (bitmap == null) {
            bitmap = getBitmapByUrl(url)
            if (bitmap != null) {
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                cache?.put(url, bos.toByteArray())
            }
        }

        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        val intent = Intent().putExtra(ImageDownloadService.BITMAP_KEY, result)
        pendingIntent.send(service.get(), 0, intent)
    }
}


private fun getBitmapByUrl(url: String?): Bitmap? {
    if (url.isNullOrBlank()) {
        return null
    }
    return try {
        URL(url).openStream().use { urlStream ->
            BitmapFactory.decodeStream(urlStream)
        }
    } catch (e: Exception) {
        Log.d("ASYNC_TASK", e.message ?: "")
        e.printStackTrace()
        null
    }
}