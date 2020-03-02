package com.colan.kindercare.data.remote.data.response.message

import android.util.Log
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class UserListResponse {

        @SerializedName("id")
        @Expose
        var id: Int? = null
        @SerializedName("detail")
        @Expose
        var detail: String? = null
        @SerializedName("email")
        @Expose
        var email: String? = null

        var isSelected: Boolean? = false

        var header:String?=null

        constructor(header: String) {
                Log.d("setheader","called")
                this.header = header
        }

        constructor(id: Int?, detail: String?, isSelected: Boolean?) {
                this.id = id
                this.detail = detail
                this.isSelected = isSelected
        }


}