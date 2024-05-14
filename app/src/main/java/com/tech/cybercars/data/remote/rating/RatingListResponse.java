package com.tech.cybercars.data.remote.rating;

import com.tech.cybercars.data.models.Rating;
import com.tech.cybercars.data.remote.base.BaseResponse;

import java.util.List;

public class RatingListResponse extends BaseResponse {
    public List<Rating> rating_list;
}
