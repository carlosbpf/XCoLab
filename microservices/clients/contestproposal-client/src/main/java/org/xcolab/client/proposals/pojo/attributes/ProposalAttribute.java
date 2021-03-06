package org.xcolab.client.proposals.pojo.attributes;

import org.xcolab.util.http.client.RestService;

public class ProposalAttribute extends AbstractProposalAttribute {

    public ProposalAttribute() {}

    public ProposalAttribute(ProposalAttribute value) {
        super(value);
    }

    public ProposalAttribute(
            Long id_,
            Long proposalid,
            Integer version,
            Integer versionwhencreated,
            String name,
            Long additionalid,
            Long numericvalue,
            String stringvalue,
            Double realvalue
    ) {
        super(id_, proposalid, version, versionwhencreated, name, additionalid,
                numericvalue, stringvalue, realvalue);
    }

    public ProposalAttribute(AbstractProposalAttribute abstractProposalAttribute,
            RestService restService) {
        super(abstractProposalAttribute);
    }
}
