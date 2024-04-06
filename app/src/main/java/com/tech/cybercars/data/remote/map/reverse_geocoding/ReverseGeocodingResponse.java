package com.tech.cybercars.data.remote.map.reverse_geocoding;

import com.google.gson.annotations.SerializedName;
import com.tech.cybercars.data.remote.base.BaseResponse;

public class ReverseGeocodingResponse extends BaseResponse {
    @SerializedName("lat")
    public Double lat;
    @SerializedName("lng")
    public Double lng;
    @SerializedName("name")
    public String name;
    @SerializedName("display_name")
    public String display_name;
    @SerializedName("address")
    public Address address;
    @SerializedName("boundingbox")
    public Double[] boundingbox;


    public class Address{
        @SerializedName("road")
        public String road;
        @SerializedName("city")
        public String city;
        @SerializedName("quarter")
        public String quarter;
        @SerializedName("district")
        public String district;
        @SerializedName("county")
        public String county;
        @SerializedName("state")
        public String state;
        @SerializedName("country")
        public String country;
        @SerializedName("country_code")
        public String country_code;
    }
}
