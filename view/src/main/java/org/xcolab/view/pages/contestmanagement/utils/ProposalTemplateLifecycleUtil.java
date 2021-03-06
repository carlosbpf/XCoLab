package org.xcolab.view.pages.contestmanagement.utils;

import org.xcolab.client.contest.PlanTemplateClientUtil;
import org.xcolab.client.contest.pojo.templates.PlanSectionDefinition;
import org.xcolab.client.contest.pojo.templates.PlanTemplate;
import org.xcolab.client.contest.pojo.templates.PlanTemplateSection;

import java.util.List;

public final class ProposalTemplateLifecycleUtil {

    private static final String DEFAULT_TEMPLATE_NAME = "New template";

    private ProposalTemplateLifecycleUtil() { }

    public static PlanTemplate create() {
        PlanTemplate newPlanTemplate = new PlanTemplate();
        newPlanTemplate.setName(DEFAULT_TEMPLATE_NAME);
        newPlanTemplate.setImpactSeriesTemplateId(1L);
        newPlanTemplate.setImpactSeriesTemplateId(1L);
        newPlanTemplate.setBaseTemplateId(0L);
        newPlanTemplate = PlanTemplateClientUtil.createPlanTemplate(newPlanTemplate);
        return newPlanTemplate;

    }

    public static void delete(Long templateId) {
        PlanTemplate planTemplate = PlanTemplateClientUtil.getPlanTemplate(templateId);
        deletePlanTemplateSections(templateId);
        deleteUnusedPlanSectionDefinitions(planTemplate);
        PlanTemplateClientUtil.deletePlanTemplate(templateId);

    }

    private static void deletePlanTemplateSections(Long planTemplateId) {
        List<PlanTemplateSection> planTemplateSections =
                PlanTemplateClientUtil.getPlanTemplateSectionByPlanTemplateId(planTemplateId);
        for (PlanTemplateSection planTemplateSection : planTemplateSections) {
            PlanTemplateClientUtil
                    .deletePlanTemplateSection(planTemplateSection.getPlanTemplateId(),
                            planTemplateSection.getPlanSectionId());
        }
    }

    private static void deleteUnusedPlanSectionDefinitions(PlanTemplate planTemplate) {
        List<PlanSectionDefinition> planSectionDefinitions = PlanTemplateClientUtil
                .getPlanSectionDefinitionByPlanTemplateId(planTemplate.getId_(), true);
        for (PlanSectionDefinition planSectionDefinition : planSectionDefinitions) {
            if (!isPlanSectionDefinitionUsedInOtherTemplate(planSectionDefinition.getId_(),
                    planTemplate.getId_())) {
                PlanTemplateClientUtil
                        .deletePlanSectionDefinition(planSectionDefinition.getId_());
            }
        }

    }

    public static boolean isPlanSectionDefinitionUsedInOtherTemplate(Long planSectionDefinitionId,
            Long planTemplateId) {
        List<PlanTemplateSection> planTemplateSections =
                PlanTemplateClientUtil
                        .getPlanTemplateSectionByPlanSectionDefinitionId(planSectionDefinitionId);
        return !(planTemplateSections.size() == 1
                && planTemplateSections.get(0).getPlanTemplateId() == planTemplateId.longValue())
                && !planTemplateSections.isEmpty();

    }
}
