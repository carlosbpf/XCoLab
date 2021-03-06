package org.xcolab.client.proposals.pojo.evaluation.members;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.core.ParameterizedTypeReference;

import org.xcolab.util.http.client.RestService;
import org.xcolab.util.http.client.types.TypeProvider;
import org.xcolab.util.http.dto.DataTransferObject;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ProposalSupporterDto extends AbstractProposalSupporter
        implements DataTransferObject<ProposalSupporter> {

    public static final TypeProvider<ProposalSupporterDto> TYPES =
            new TypeProvider<>(ProposalSupporterDto.class,
                    new ParameterizedTypeReference<List<ProposalSupporterDto>>() {
                    });

    public ProposalSupporterDto() {}

    public ProposalSupporterDto(AbstractProposalSupporter value) {
        super(value);
    }

    @Override
    public ProposalSupporter toPojo(RestService restService) {
        return new ProposalSupporter(this, restService);
    }
}
