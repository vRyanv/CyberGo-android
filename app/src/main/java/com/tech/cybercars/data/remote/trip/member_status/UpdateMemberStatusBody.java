package com.tech.cybercars.data.remote.trip.member_status;

public class UpdateMemberStatusBody {
    public String trip_id;
    public Member member;

    public String status;

    public UpdateMemberStatusBody(String trip_id, Member member, String status) {
        this.trip_id = trip_id;
        this.member = member;
        this.status = status;
    }

    public static class Member{
        public String member_id;
        public String user_id;
    }

}
