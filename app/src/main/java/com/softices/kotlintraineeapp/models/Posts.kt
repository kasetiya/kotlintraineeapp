package com.softices.kotlintraineeapp.models

import java.io.Serializable

class Posts : Serializable {
    var id: Int = 0
    var UserId: Int = 0
    var title: String? = null
    var body: String? = null

    constructor() {}
    constructor(title: String, body: String, id: Int, UserId: Int) {
        this.title = title
        this.body = body
        this.id = id
        this.UserId = UserId
    }
}