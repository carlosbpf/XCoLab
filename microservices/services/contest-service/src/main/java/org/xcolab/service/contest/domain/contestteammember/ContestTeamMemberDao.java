package org.xcolab.service.contest.domain.contestteammember;

import org.xcolab.model.tables.pojos.ContestTeamMember;
import org.xcolab.service.contest.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ContestTeamMemberDao {
    boolean delete(Long id_);

    ContestTeamMember create(ContestTeamMember contestTeamMember);

    Optional<ContestTeamMember> get(Long id_) throws NotFoundException;

    boolean exists(Long id_);

    boolean update(ContestTeamMember contestTeamMember);

    List<ContestTeamMember> findByGiven(Long contestId);

    ContestTeamMember findOneBy(Long memberId, Long contestId, Long roleId);
}
