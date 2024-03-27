package com.fullcycle.admin.catalogo.infrastructure.castmembers.persistence;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import javax.persistence.*;
import java.time.Instant;

/**
 * @author kalil.peixoto
 * @date 3/15/23 20:53
 * @email kalilmvp@gmail.com
 */
@Entity(name = "CastMembers")
@Table(name = "cast_members")
public class CastMembersJPAEntity {

    @Id
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CastMemberType type;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public CastMembersJPAEntity() {
    }

    private CastMembersJPAEntity(final String id, final String name, final CastMemberType type,
                                 final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMembersJPAEntity from(CastMember aCastMember) {
        return new CastMembersJPAEntity(
                aCastMember.getId().getValue(),
                aCastMember.getName(),
                aCastMember.getType(),
                aCastMember.getCreatedAt(),
                aCastMember.getUpdatedAt());
    }

    public CastMember toAggregate() {
        return CastMember.with(CastMemberID.from(this.getId()),
                this.getName(),
                this.getType(),
                this.getCreatedAt(),
                this.getUpdatedAt());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public CastMemberType getType() {
        return type;
    }

    public void setType(CastMemberType type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
