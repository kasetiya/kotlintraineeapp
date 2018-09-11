package com.softices.kotlintraineeapp.models

import java.io.Serializable

/***
 *
 */
class ContactModel() : Serializable {
    var contactID: String = ""
    var contactName: String = ""
    var contactNumber: String = ""
    var contactEmail: String = ""
    var contactPhoto: String = ""
    var contactOtherDetails: String = ""

    constructor(contactID: String, contactName: String, contactNumber: String, contactEmail: String, contactPhoto: String, contactOtherDetails: String) : this() {
        this.contactID = contactID
        this.contactName = contactName
        this.contactNumber = contactNumber
        this.contactEmail = contactEmail
        this.contactPhoto = contactPhoto
        this.contactOtherDetails = contactOtherDetails
    }
}