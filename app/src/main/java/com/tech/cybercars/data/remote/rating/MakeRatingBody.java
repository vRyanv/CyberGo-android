package com.tech.cybercars.data.remote.rating;

public class MakeRatingBody {
    public String user_receive;
    public int star;
    public String comment;

    public MakeRatingBody(String user_receive, int star, String comment) {
        this.user_receive = user_receive;
        this.star = star;
        this.comment = comment;
    }
}
