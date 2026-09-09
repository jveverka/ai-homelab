package com.example.iamservice.repository;

import com.example.iamservice.repository.domain.TokenRow;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.iamservice.repository.Tables.*;

@Repository
public class TokenRepository {

    private final DSLContext dsl;

    public TokenRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public TokenRow insert(TokenRow token) {
        dsl.insertInto(TOKENS)
                .set(TK_TOKEN, token.token())
                .set(TK_EMAIL, token.email())
                .set(TK_PERMISSIONS, token.permissions())
                .set(TK_EXPIRES_AT, token.expiresAt())
                .execute();
        return token;
    }

    public Optional<TokenRow> findById(UUID token) {
        return Optional.ofNullable(dsl.selectFrom(TOKENS)
                .where(TK_TOKEN.eq(token))
                .fetchOne(this::toRow));
    }

    public void deleteById(UUID token) {
        dsl.deleteFrom(TOKENS)
                .where(TK_TOKEN.eq(token))
                .execute();
    }

    public List<TokenRow> findByPermission(String permission) {
        // PostgreSQL: array_position(permissions, 'some_perm') IS NOT NULL
        return dsl.selectFrom(TOKENS)
                .where(DSL.function("array_position", Integer.class, TK_PERMISSIONS, DSL.inline(permission)).isNotNull())
                .fetch(this::toRow);
    }

    public List<TokenRow> findByEmail(String email) {
        return dsl.selectFrom(TOKENS)
                .where(TK_EMAIL.eq(email))
                .fetch(this::toRow);
    }

    private TokenRow toRow(org.jooq.Record record) {
        if (record == null) return null;
        return new TokenRow(
                record.get(TK_TOKEN),
                record.get(TK_EMAIL),
                record.get(TK_PERMISSIONS),
                record.get(TK_EXPIRES_AT)
        );
    }
}
