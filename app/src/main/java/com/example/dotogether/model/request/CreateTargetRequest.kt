package com.example.dotogether.model.request

class CreateTargetRequest(
    target: String,
    description: String,
    period: String,
    start_date: String,
    end_date: String,
    img: String?,
    tags: String,
    private: Boolean
) : TargetRequest(
    target,
    description,
    period,
    start_date,
    end_date,
    img,
    tags,
    private
)