package kt.fellowweeb.fakeapiboard

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.post_creation_layout.view.*
import kotlinx.android.synthetic.main.post_layout.view.*
import kotlinx.android.synthetic.main.post_layout.view.postBody
import kotlinx.android.synthetic.main.post_layout.view.postTitle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kt.fellowweeb.fakeapiboard.model.Post
import kt.fellowweeb.fakeapiboard.service.PostService
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: PostAdapter
    private lateinit var dialog: Dialog

    private lateinit var postService: PostService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postService = App.instance.postService

        adapter = PostAdapter(
            mutableListOf()
        ) { position ->
            val post = adapter.posts[position]

            post.id?.also { id ->
                lifecycle.coroutineScope.launch(Dispatchers.Main) {
                    try {
                        progressBar.show()
                        postService.deletePost(id).toastMessageOrErrorCode(getString(R.string.post_deleted))
                    } catch (e: Exception) {
                        toastShort(getString(R.string.cant_connect))
                    } finally {
                        progressBar.hide()
                    }
                }
            }
            realm().executeTransactionAsync { realm ->
                realm.where<Post>()
                    .equalTo("title", post.title)
                    .equalTo("body", post.body)
                    .findFirst()?.deleteFromRealm()
            }
            adapter.posts.removeAt(position)
            adapter.notifyItemRemoved(position)
        }

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter

        initDialog()

        fab.setOnClickListener {
            dialog.show()
        }

        savedInstanceState?.also {
            progressBar.hide()
            onRestoreInstanceState(it)
        } ?: sync()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.resync -> {
            resync()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(POSTS_KEY, ArrayList(adapter.posts))
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.getParcelableArrayList<Post>(POSTS_KEY)?.run {
            adapter.posts = toMutableList()
            adapter.notifyDataSetChanged()
        }
    }

    private fun initDialog() {
        dialog = Dialog(this)
        dialog.setContentView(R.layout.post_creation_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.TOP)
        val root = dialog.findViewById<ImageButton>(R.id.sendButton).rootView
        root.progressBar.hide()
        root.sendButton.setOnClickListener {
            root.progressBar.show()
            val post = Post(
                title = root.postTitle.text.toString(),
                body = root.postBody.text.toString()
            )
            realm().executeTransactionAsync { realm ->
                val dataObject = realm.createObject<Post>()
                dataObject.title = post.title
                dataObject.body = post.body
            }
            lifecycle.coroutineScope.launch(Dispatchers.Main) {
                try {
                    root.progressBar.show()
                    postService.createPost(post).toastMessageOrErrorCode(getString(R.string.post_created))
                } catch (e: Exception) {
                    toastShort(getString(R.string.cant_connect))
                } finally {
                    root.progressBar.hide()
                }
            }
            adapter.posts.add(post)
            adapter.notifyItemInserted(adapter.posts.size - 1)
        }
    }

    private fun toastShort(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun sync() {
        progressBar.show()
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            val realm = realm()
            val findAll = realm.where<Post>().findAll().map { realm.copyFromRealm(it) }.toMutableList()
            withContext(Dispatchers.Main) {
                adapter.posts = findAll
                progressBar.hide()
                toastShort(getString(R.string.loaded_posts))
            }
        }
    }

    private fun resync() {
        progressBar.show()
        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            try {
                progressBar.show()
                val response = postService.listPosts()
                if (response.isSuccessful) {
                    val posts: List<Post> = response.body() ?: ArrayList()
                    realm().executeTransactionAsync { realm ->
                        realm.deleteAll()
                        realm.copyToRealm(posts)
                    }
                    adapter.posts = posts.toMutableList()
                    toastShort(getString(R.string.loaded_posts))
                } else {
                    toastShort(getString(R.string.error) + ": ${response.code()}")
                }
            } catch (e: Exception) {
                toastShort(getString(R.string.cant_connect))
            } finally {
                progressBar.hide()
            }
        }
    }

    private fun <T> Response<T>.toastMessageOrErrorCode(message: String) {
        if (isSuccessful) {
            toastShort(message)
        } else {
            toastShort(getString(R.string.error) + ": ${code()}")
        }
    }
}


class PostAdapter(
    posts: MutableList<Post>,
    private val onRemove: (Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        fun bind(post: Post) {
            root.postTitle.text = post.title
            root.postBody.text = post.body
        }
    }

    var posts: MutableList<Post> = posts
        set(list) {
            val diffResult = DiffUtil.calculateDiff(PostDiffUtilCallback(posts, list), false)
            field = list
            diffResult.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val postView = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_layout, parent, false)
        return PostViewHolder(postView).apply {
            root.removeButton.setOnClickListener {
                onRemove(adapterPosition)
            }
        }
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size
}

class PostDiffUtilCallback(
    private val oldList: List<Post>,
    private val newList: List<Post>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean = true
}

private fun realm() = Realm.getDefaultInstance()

const val POSTS_KEY = "POSTS_KEY"
