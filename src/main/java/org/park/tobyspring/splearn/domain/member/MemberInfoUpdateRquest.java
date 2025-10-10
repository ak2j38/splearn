package org.park.tobyspring.splearn.domain.member;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MemberInfoUpdateRquest(
    @Size(min = 5, max = 20) String nickname,
    @NotNull @Size(max = 15) String profileAddress,
    @NotNull String introduction
) {

}
