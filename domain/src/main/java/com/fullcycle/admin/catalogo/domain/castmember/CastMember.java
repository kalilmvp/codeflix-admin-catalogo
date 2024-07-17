package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.AggregateRoot;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handlers.Notification;

import java.time.Instant;
import java.util.Objects;

/**
 * @author kalil.peixoto
 * @date 2/15/23 14:07
 * @email kalilmvp@gmail.com
 */
public class CastMember extends AggregateRoot<CastMemberID> implements Cloneable {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(final CastMemberID id,
                       final String name,
                       final CastMemberType type,
                       final Instant createdAt,
                       final Instant updatedAt) {
        super(id);
        this.name = name;
        this.type = type;
        this.createdAt = Objects.requireNonNull(createdAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updatedAt' should not be null");;
        this.selfValidate();
    }

    public static CastMember newCastMember(final String name, final CastMemberType type) {
        final Instant now = InstantUtils.now();
        return new CastMember(CastMemberID.unique(), name, type, now, now);
    }

    public static CastMember with(final CastMemberID anId, final String name, final CastMemberType type,
                                  final Instant createdAt, final Instant updatedAt) {
        return new CastMember(
                anId,
                name,
                type,
                createdAt,
                updatedAt
        );
    }

    public static CastMember with(final CastMember aCastMember) {
        return with(
                aCastMember.getId(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt()
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CastMemberValidator(this, handler).validate();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to validate aggregate CastMember", notification);
        }
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();

        this.selfValidate();

        return this;
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public CastMember clone() {
        try {
            return (CastMember) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
