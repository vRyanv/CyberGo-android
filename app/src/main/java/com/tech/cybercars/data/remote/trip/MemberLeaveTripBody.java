package com.tech.cybercars.data.remote.trip;

public class MemberLeaveTripBody {
    public String trip_id;
    public String member_id;
    public String member_full_name;
    public String member_avatar;

    public MemberLeaveTripBody(String trip_id, String member_id, String member_full_name, String member_avatar) {
        this.trip_id = trip_id;
        this.member_id = member_id;
        this.member_full_name = member_full_name;
        this.member_avatar = member_avatar;
    }
}
