package kt.fellowweeb.images

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.image_list_item.view.*
import kt.fellowweeb.images.data.UnsplashPhoto


class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = ImageAdapter(
            emptyList()
            ) { image ->
            val intent = Intent(this, FullImageActivity::class.java).apply {
                putExtra(IMAGE_URL_KEY, image.urls.full)
            }
            startActivity(intent)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
        
        savedInstanceState?.also { onRestoreInstanceState(it) } ?: redownloadImageList()
    }
    
    private fun redownloadImageList() {
        val pendingIntent = createPendingResult(ImageDownloadService.DOWNLOAD_IMAGE_LIST, Intent(), 0)
        val serviceIntent = Intent(this, ImageDownloadService::class.java)
            .putExtra(ImageDownloadService.COMMAND_KEY, ImageDownloadService.DOWNLOAD_IMAGE_LIST)
            .putExtra(ImageDownloadService.PENDING_INTENT_KEY, pendingIntent)
        startService(serviceIntent)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val images = data?.getParcelableExtra<ImageListHolder>(IMAGES_KEY)
        if (images != null) {
            adapter.images = images.images
            adapter.notifyDataSetChanged()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(IMAGES_KEY, ImageListHolder(adapter.images))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelable<ImageListHolder>(IMAGES_KEY)?.apply {
            adapter.images = images
            adapter.notifyDataSetChanged()
        }
    }
}

class ImageAdapter(
    var images: List<UnsplashPhoto>,
    private val onClick: (UnsplashPhoto) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    class ImageViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(image: UnsplashPhoto) {
            image.description?.also {
                root.description_text_view.text = it
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item, parent, false)
        return ImageViewHolder(imageView).apply {
            root.setOnClickListener {
                onClick(images[adapterPosition])
            }
        }
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}
