package com.ctemplar.app.fdroid.repository;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import com.ctemplar.app.fdroid.repository.entity.ContactEntity;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM contacts ORDER BY id")
    List<ContactEntity> getAll();

    @Insert(onConflict = REPLACE)
    void save(ContactEntity contactEntity);

    @Query("DELETE FROM contacts")
    void deleteAll();

    @Query("DELETE FROM contacts WHERE id = :id")
    void delete(long id);

    @Query("SELECT * FROM contacts WHERE id = :id")
    ContactEntity getById(long id);
}