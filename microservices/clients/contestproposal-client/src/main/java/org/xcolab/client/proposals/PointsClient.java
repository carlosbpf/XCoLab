package org.xcolab.client.proposals;

import org.xcolab.client.proposals.pojo.points.PointType;
import org.xcolab.client.proposals.pojo.points.PointTypeDto;
import org.xcolab.client.proposals.pojo.points.PointsDistributionConfiguration;
import org.xcolab.client.proposals.pojo.points.PointsDistributionConfigurationDto;
import org.xcolab.util.http.caching.CacheKeys;
import org.xcolab.util.http.caching.CacheRetention;
import org.xcolab.util.http.client.RestResource1;
import org.xcolab.util.http.client.RestService;
import org.xcolab.util.http.dto.DtoUtil;
import org.xcolab.util.http.exceptions.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PointsClient {

    private static final Map<RestService, PointsClient> instances = new HashMap<>();

    private final RestService proposalService;

    private final RestResource1<PointsDistributionConfigurationDto, Long>
            pointsDistributionConfigurationResource;
    private final RestResource1<PointTypeDto, Long> pointTypeResource;

    private PointsClient(RestService proposalService) {
        pointsDistributionConfigurationResource = new RestResource1<>(proposalService,
        "pointsDistributionConfigurations", PointsDistributionConfigurationDto.TYPES);
        pointTypeResource = new RestResource1<>(proposalService,
                "pointTypes", PointTypeDto.TYPES);
        this.proposalService = proposalService;
    }

    public static PointsClient fromService(RestService proposalService) {
        PointsClient instance = instances.get(proposalService);
        if (instance == null) {
            instance = new PointsClient(proposalService);
            instances.put(proposalService, instance);
        }
        return instance;
    }

    public PointsDistributionConfiguration createPointsDistributionConfiguration(
            PointsDistributionConfiguration pointsDistributionConfiguration) {
        return pointsDistributionConfigurationResource
                .create(new PointsDistributionConfigurationDto(pointsDistributionConfiguration))
                .execute().toPojo(proposalService);
    }

    public PointsDistributionConfiguration
    getPointsDistributionConfigurationByTargetPlanSectionDefinitionId(
            long targetPlanSectionDefinitionId) {
        try {
            return pointsDistributionConfigurationResource
                    .service("getByTargetPlanSectionDefinitionId",
                            PointsDistributionConfigurationDto.class)
                    .queryParam("targetPlanSectionDefinitionId", targetPlanSectionDefinitionId)
                    .getChecked().toPojo(proposalService);
        } catch (EntityNotFoundException ignored){
            return null;
        }

    }

    public boolean updatePointsDistributionConfiguration(
            PointsDistributionConfiguration pointsDistributionConfiguration) {
        return pointsDistributionConfigurationResource
                .update(new PointsDistributionConfigurationDto(pointsDistributionConfiguration),
                        pointsDistributionConfiguration.getId_())
                .execute();
    }

    public List<PointsDistributionConfiguration> getPointsDistributionByProposalIdPointTypeId(
            Long proposalId, Long pointTypeId) {
        return DtoUtil.toPojos(pointsDistributionConfigurationResource.list()
                .optionalQueryParam("proposalId", proposalId)
                .optionalQueryParam("pointTypeId", pointTypeId)
                .execute(), proposalService);
    }

    public Boolean deletePointsDistributionConfiguration(Long pointsDistributionConfigurationId) {
        return pointsDistributionConfigurationResource.delete(pointsDistributionConfigurationId)
                .execute();
    }

    public void verifyDistributionConfigurationsForProposalId(Long proposalId) {
         pointsDistributionConfigurationResource.service("verifyDistributionConfigurationsForProposalId", String.class)
                .queryParam("proposalId", proposalId).execute();
    }

    public Boolean deletePointsDistributionConfigurationByProposalId(Long proposalId) {
        return pointsDistributionConfigurationResource.service("removeByProposalId", Boolean.class)
                .queryParam("proposalId", proposalId).execute();
    }

    public PointType getPointType(long Id_) {
        return pointTypeResource.get(Id_)
                .withCache(CacheKeys.of(PointTypeDto.class, Id_), CacheRetention.REQUEST)
                .execute().toPojo(proposalService);

    }

    public List<PointType> getAllPointTypes() {
        return DtoUtil.toPojos(pointTypeResource.list()
                .execute(), proposalService);
    }

    public List<PointType> getChildrenOfPointType(Long parentPointTypeId) {
        return DtoUtil.toPojos(pointTypeResource.list()
                .withCache(CacheKeys.withClass(PointTypeDto.class)
                                .withParameter("parentPointTypeId", parentPointTypeId)
                                .asList(),
                        CacheRetention.MEDIUM)
                .optionalQueryParam("parentPointTypeId", parentPointTypeId)
                .execute(), proposalService);
    }
}
