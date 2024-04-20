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

    @Query("DELETE FROM member")
    public void ClearTable();
}
