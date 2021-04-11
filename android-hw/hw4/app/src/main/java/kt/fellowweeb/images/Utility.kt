package kt.fellowweeb.images

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kt.fellowweeb.images.data.UnsplashPhoto

const val API_URL = "https://api.unsplash.com/photos/random"
const val API_TOKEN = "_RYqx1nldXnBIkSI93NpgXFhG9wC4t6dxGg6vqtQTco"

const val IMAGE_KEY = "IMAGE"
const val IMAGES_KEY = "IMAGES"
const val IMAGE_URL_KEY = "IMAGE_URL"


@Parcelize
class ImageListHolder(val images: List<UnsplashPhoto>) : Parcelable