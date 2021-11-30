package com.dn.nhc.remote.network

import com.dn.nhc.remote.model.*
import com.dn.nhc.remote.model.common.Image
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.HashMap

interface NeighborhoodChoresApi {

    //로그인
    @GET("api/users/nhc-login")
    suspend fun getLoginAuth(
        @Query("social_id") socialId: String,
        @Query("device_token") deviceToken: String
    ): LoginResponse

    //로그아웃
    @POST("api/users/nhc-logout")
    suspend fun postLogout(
    ): CommonResponse

    //회원가입
    @FormUrlEncoded
    @POST("api/users")
    suspend fun postJoin(
        @FieldMap parameters: HashMap<String, Any>,
    ): UserResponse

    //게시글 등록
    @Multipart
    @POST("api/posts")
    suspend fun postPheedUpload(
        @Part files: List<MultipartBody.Part>,
        @Part tags: List<MultipartBody.Part>,
        @PartMap partMap: HashMap<String, RequestBody>
    ): PheedUploadResponse

    //게시글 삭제
    @DELETE("api/posts")
    suspend fun deletePheed(
        @Query("post_id") postId: String,
    ): CommonResponse

    //게시글 수정
    @Multipart
    @PUT("api/posts/{post_id}")
    suspend fun putUpdatePheed(
        @Path("post_id") postId: String,
        @Part files: List<MultipartBody.Part>,
        @Part tags: List<MultipartBody.Part>,
        @Part("existing_images") existingImages: List<Image>,
        @PartMap partMap: HashMap<String, RequestBody>
    ): PheedUploadResponse

    //피드 리스트
    @GET("api/posts/all")
    suspend fun getPheedList(
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PheedResponse

    //컨텐츠 검색 결과 피드 리스트
    @GET("api/search")
    suspend fun getSearchContentPheedListResult(
        @Query("query") query: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PheedResponse

    //해시태그 검색 결과 피드 리스트
    @GET("api/search/hashtag")
    suspend fun getSearchHashTagPheedListResult(
        @Query("query") query: String,
        @Query("size") size: Int,
        @Query("page") page: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): PheedResponse

    //피드 상세
    @GET("api/posts")
    suspend fun getPheedDetail(
        @Query("post_id") postId: String,
    ): PheedUploadResponse

    //피드 댓글 조회
    @GET("api/comments/post")
    suspend fun getComment(
        @Query("post_id") postId: String,
    ): CommentListResponse

    //마이페이지 유저
    @GET("api/users")
    suspend fun getMypageInfo(
    ): LoginResponse

    //마이페이지 내 피드
    @GET("api/mypage")
    suspend fun getMypagePheed(
        @Query("size") size: Int,
        @Query("page") page: Int
    ): PheedResponse

    //댓글 등록
    @FormUrlEncoded
    @POST("api/comments")
    suspend fun postComment(
        @FieldMap parameters: HashMap<String, Any>
    ): CommentResponse

    //댓글 삭제
    @DELETE("api/comments")
    suspend fun deleteComment(
        @Query("comment_id") commentId: String,
    ): CommonResponse

    //피드 상세 댓글
    @GET("api/comments")
    suspend fun getCommentDetail(
        @Query("comment_id") commentId: String,
    ): CommentListResponse

    //신고하기
    @FormUrlEncoded
    @POST("api/blames/{target_type}")
    suspend fun postComplain(
        @Path("target_type") targetType: String,
        @FieldMap parameters: HashMap<String, Any>
    ): CommonResponse

    //프로필 사진 변경
    @Multipart
    @PUT("api/users")
    suspend fun putUpdateProfileImage(
        @Part files: MultipartBody.Part
    ): UserResponse

    //알람 리스트
    @GET("api/users/pushes")
    suspend fun getPushes(
        @Query("size") size: Int,
        @Query("page") page: Int
    ): PushesResponse

    //알람 읽음 처리
    @PATCH("api/pushes/read")
    suspend fun patchReadPush(
        @Query("push_id") size: Int
    ): CommonResponse

}