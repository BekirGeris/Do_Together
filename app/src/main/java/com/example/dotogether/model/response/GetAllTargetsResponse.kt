package com.example.dotogether.model.response

import com.example.dotogether.model.Link
import com.example.dotogether.model.Target

class GetAllTargetsResponse(
    var current_page: Int? = null,
    var data: ArrayList<Target>? = null,
    var first_page_url: String? = null,
    var from: Int? = null,
    var last_page: Int? = null,
    var last_page_url: String? = null,
    var links: List<Link>? = null,
    var next_page_url: String? = null,
    var path: String? = null,
    var per_page: Int? = null,
    var prev_page_url: String? = null,
    var to: Int? = null,
    var total: Int? = null
)