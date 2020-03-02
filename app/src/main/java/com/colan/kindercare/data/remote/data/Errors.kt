package com.colan.kindercare.data.remote.data

import com.google.gson.annotations.SerializedName

data class Errors(@SerializedName("msg")
                  var message: String)