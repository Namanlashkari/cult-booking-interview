package com.cultfit.model;

import java.util.Objects;

/**
 * A studio member who can book classes. The tier determines the discount
 * applied at checkout.
 */
public class Member {
    private final String id;
    private final String name;
    private final MembershipTier tier;

    public Member(String id, String name, MembershipTier tier) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.tier = Objects.requireNonNull(tier, "tier");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MembershipTier getTier() {
        return tier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{id=" + id + ", name=" + name + ", tier=" + tier + '}';
    }
}
