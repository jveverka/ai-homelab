package com.example.iamservice.repository;

import com.example.iamservice.repository.domain.UserRow;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.iamservice.repository.Tables.*;

@Repository
public class UserRepository {

    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public UserRow findById(UUID id) {
        return dsl.selectFrom(USERS)
                .where(USR_ID.eq(id))
                .fetchOne(this::toRow);
    }

    public Optional<UserRow> findByEmail(String email) {
        return Optional.ofNullable(dsl.selectFrom(USERS)
                .where(USR_EMAIL.eq(email))
                .fetchOne(this::toRow));
    }

    public UserRow insert(UserRow user) {
        dsl.insertInto(USERS)
                .set(USR_ID, user.id())
                .set(USR_EMAIL, user.email())
                .set(USR_CREATED_AT, user.createdAt())
                .set(USR_ACTIVE, user.active())
                .set(USR_PWDHASH, user.pwdhash())
                .execute();
        return user;
    }

    public void updateActive(UUID id, boolean active) {
        dsl.update(USERS)
                .set(USR_ACTIVE, active)
                .where(USR_ID.eq(id))
                .execute();
    }

    public void deleteById(UUID id) {
        dsl.deleteFrom(USERS)
                .where(USR_ID.eq(id))
                .execute();
    }

    public List<UserRow> findAll() {
        return dsl.selectFrom(USERS)
                .orderBy(USR_EMAIL.asc())
                .fetch(this::toRow);
    }

    public long count() {
        return dsl.fetchCount(USERS);
    }

    public long countAdmins() {
        var record = dsl.select(DSL.countDistinct(USR_ID))
                .from(USERS)
                .join(USER_PERMISSIONS).on(UP_USER_ID.eq(USR_ID))
                .where(UP_PERMISSION_ID.in("iam.users.read", "iam.users.create"))
                .fetchOne();
        return record != null ? record.value1().longValue() : 0L;
    }

    private UserRow toRow(org.jooq.Record record) {
        if (record == null) return null;
        return new UserRow(
                record.get(USR_ID),
                record.get(USR_EMAIL),
                record.get(USR_CREATED_AT),
                record.get(USR_ACTIVE),
                record.get(USR_PWDHASH)
        );
    }
}
