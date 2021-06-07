package com.gmind.smartgate.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
        var berhasil : String ?="",
        var nomor : String ?="",
        var gagal : String ?="",
        var masjid : String ?="",
        var nama : String ?="",
        var password : String ?="",
        var url : String ?="",
        var suhu : String ?="",
        var username : String ?="",
        var trobos : String ?=""
) : Parcelable
