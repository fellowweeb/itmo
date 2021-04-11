package kt.fellowweeb.fakeapiboard.service

import kt.fellowweeb.fakeapiboard.model.Post
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface PostService {
    @GET("posts")
    suspend fun listPosts() : Response<List<Post>>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int) : Response<Unit>

    @POST("posts")
    suspend fun createPost(@Body post: Post) : Response<Post>
}