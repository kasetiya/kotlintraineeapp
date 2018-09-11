package com.softices.kotlintraineeapp.models

import android.graphics.Bitmap
import java.io.Serializable

class UserModel : Serializable {
    var firstName: String? = null
    var lastName: String? = null
    var email: String? = null
    var mobile: String? = null
    var password: String? = null
    var photo: Bitmap? = null

    constructor() {}
    constructor(firstName: String, lastName: String, email: String, mobile: String, password: String, photo: Bitmap) {
        this.firstName = firstName
        this.lastName = lastName
        this.email = email
        this.mobile = mobile
        this.password = password
        this.photo = photo
    }
}