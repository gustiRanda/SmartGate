package com.gmind.smartgate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    var berhasil : String ?="",
    var email : String ?="",
    var gagal : String ?="",
    var masjid : String ?="",
    var nama : String ?="",
    var password : String ?="",
    var url : String ?="",
    var username : String ?=""
) : Parcelable
