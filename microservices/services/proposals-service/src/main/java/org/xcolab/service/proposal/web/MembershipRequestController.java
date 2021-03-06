package org.xcolab.service.proposal.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xcolab.model.tables.pojos.MembershipRequest;
import org.xcolab.service.proposal.domain.membershiprequest.MembershipRequestDao;
import org.xcolab.service.proposal.exceptions.NotFoundException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
public class MembershipRequestController {

    @Autowired
    private MembershipRequestDao membershipRequestDao;

    @RequestMapping(value = "/membershipRequests", method = RequestMethod.POST)
    public MembershipRequest createMembershipRequest(@RequestBody MembershipRequest membershipRequest) {
            membershipRequest.setCreateDate(new Timestamp(new Date().getTime()));
        return this.membershipRequestDao.create(membershipRequest);
    }

    @RequestMapping(value = "/membershipRequests/{membershipRequestId}", method = RequestMethod.GET)
    public MembershipRequest getMembershipRequest(@PathVariable("membershipRequestId") Long membershipRequestId) throws NotFoundException {
        if (membershipRequestId == null || membershipRequestId == 0) {
            throw new NotFoundException("No membershipRequestId given");
        } else {
            return membershipRequestDao.get(membershipRequestId);
        }
    }

    @RequestMapping(value = "/membershipRequests/{membershipRequestId}", method = RequestMethod.PUT)
    public boolean updateMembershipRequest(@RequestBody MembershipRequest membershipRequest,
                                           @PathVariable("membershipRequestId") Long membershipRequestId) throws NotFoundException {

        if (membershipRequestId == null || membershipRequestId == 0 || membershipRequestDao.get(membershipRequestId) == null) {
            throw new NotFoundException("No MembershipRequest with id " + membershipRequestId);
        } else {
            return membershipRequestDao.update(membershipRequest);
        }
    }



    @RequestMapping(value = "/membershipRequests", method = {RequestMethod.GET, RequestMethod.HEAD})
    public List<MembershipRequest> getMembershipRequests(
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Integer statusId,
            @RequestParam(required = false) Long userId
    ) {
        return membershipRequestDao.findByGiven(groupId, statusId, userId);
    }


}
