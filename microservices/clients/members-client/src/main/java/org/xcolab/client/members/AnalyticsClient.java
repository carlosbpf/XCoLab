package org.xcolab.client.members;

import org.xcolab.client.members.pojo.AnalyticsUserEvent;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.util.clients.CoLabService;
import org.xcolab.util.http.client.RestResource1;
import org.xcolab.util.http.client.RestResource2L;
import org.xcolab.util.http.client.RestService;

public final class AnalyticsClient {

    private static final RestService memberService = new RestService(CoLabService.MEMBER);

    private static final RestResource1<Member, Long> memberResource =
            new RestResource1<>(memberService, "members", Member.TYPES);

    private static final RestResource2L<Member, AnalyticsUserEvent> analyticsUserEventResource =
            new RestResource2L<>(memberResource, "analyticsEvents", AnalyticsUserEvent.TYPES);

    public static AnalyticsUserEvent create(long memberId, String idString, String category,
            String action, String label, int value) {
        AnalyticsUserEvent analyticsUserEvent = new AnalyticsUserEvent();
        analyticsUserEvent.setUserId(memberId);
        analyticsUserEvent.setIdString(idString);
        analyticsUserEvent.setCategory(category);
        analyticsUserEvent.setAction(action);
        analyticsUserEvent.setLabel(label);
        analyticsUserEvent.setValue(value);

        return analyticsUserEventResource.resolveParent(memberResource.id(memberId))
                .create(analyticsUserEvent).execute();
    }

    public static boolean exists(long memberId, String idString) {
        return analyticsUserEventResource.resolveParent(memberResource.id(memberId))
                .service(idString, "exists", Boolean.class)
                .get();
    }
}
