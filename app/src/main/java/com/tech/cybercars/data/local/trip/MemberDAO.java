package com.tech.cybercars.data.local.trip;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.tech.cybercars.data.models.Member;

import java.util.List;

@Dao
public interface MemberDAO {
    @Insert
    void InsertMembers(List<Member> members);

    @Query("SELECT * FROM member as m, user as u WHERE m.user_id == u.user_id AND m.trip_id == :trip_id")
    public List<Member> FindMemberByTripId(String trip_id);

    @Query("DELETE FROM member")
    public void ClearTable();

}
