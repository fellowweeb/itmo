package kt.fellowweeb.images

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import kotlinx.android.synthetic.main.full_image.*
import kt.fellowweeb.images.ImageDownloadService.Companion.BITMAP_KEY
import kt.fellowweeb.images.ImageDownloadService.Companion.COMMAND_KEY
import kt.fellowweeb.images.ImageDownloadService.Companion.DOWNLOAD_IMAGE_BITMAP
import kt.fellowweeb.images.ImageDownloadService.Companion.IMAGE_URL_KEY
import kt.fellowweeb.images.ImageDownloadService.Companion.PENDING_INTENT_KEY

class FullImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.full_image)
        onNewIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        photo_view.setImageResource(android.R.color.transparent)
        progress_circular.show()
        val url: String = intent?.getStringExtra(kt.fellowweeb.images.IMAGE_URL_KEY) ?: ""
        val pendingIntent = createPendingResult(DOWNLOAD_IMAGE_BITMAP, Intent(), 0)
        val serviceIntent = Intent(this, ImageDownloadService::class.java)
            .putExtra(IMAGE_URL_KEY, url)
            .putExtra(COMMAND_KEY, DOWNLOAD_IMAGE_BITMAP)
            .putExtra(PENDING_INTENT_KEY, pendingIntent)
        startService(serviceIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val bitmap = data?.getParcelableExtra<Bitmap>(BITMAP_KEY)
        progress_circular.hide()
        if (bitmap != null) {
            photo_view.setImageBitmap(bitmap)
        } else {
            photo_view.setImageResource(R.drawable.broken_image_24)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val drawable = photo_view.drawable
        if (drawable != null) {
            outState.putParcelable(IMAGE_KEY, drawable.toBitmap())
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<Bitmap>(IMAGE_KEY)?.also {
            progress_circular.hide()
            photo_view.setImageBitmap(it)
        }
    }
}


