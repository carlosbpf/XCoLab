package org.xcolab.view.pages.contestmanagement.controller.manager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.xcolab.client.admin.EmailTemplateClientUtil;
import org.xcolab.client.admin.pojo.ContestEmailTemplate;
import org.xcolab.view.errors.ErrorText;
import org.xcolab.view.pages.contestmanagement.entities.ContestManagerTabs;
import org.xcolab.view.pages.contestmanagement.entities.LabelStringValue;
import org.xcolab.view.pages.contestmanagement.utils.SetRenderParameterUtil;
import org.xcolab.view.pages.contestmanagement.wrappers.EmailTemplateWrapper;
import org.xcolab.view.taglibs.xcolab.wrapper.TabWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/contest/manager")
public class ContestManagerEmailTemplateTabController extends ContestManagerBaseTabController {

    static final private ContestManagerTabs tab = ContestManagerTabs.EMAIL_TEMPLATES;
    static final private String TAB_VIEW = "contestmanagement/manager/emailTab";

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(HttpServletRequest request) {
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @GetMapping("tab/EMAIL_TEMPLATES")
    public String showEmailTabController(HttpServletRequest request, HttpServletResponse response,
            Model model,
            @RequestParam(required = false) String elementId) {
        if (!tabWrapper.getCanView()) {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }

        String templateType = elementId != null ? elementId : getFirstTemplateName();
        model.addAttribute("templateType", templateType);
        if (!StringUtils.isBlank(templateType)) {
            model.addAttribute("emailTemplateWrapper",
                    new EmailTemplateWrapper(templateType));
        }
        final List<ContestEmailTemplate> emailTemplates = EmailTemplateClientUtil
                .listAllContestEmailTemplates();
        List<LabelStringValue> templateSelectionItems = new ArrayList<>();
        for (ContestEmailTemplate emailTemplate : emailTemplates) {
            templateSelectionItems.add(new LabelStringValue(emailTemplate.getType_(),
                    emailTemplate.getType_()));
        }
        model.addAttribute("templateSelectionItems", templateSelectionItems);
        setPageAttributes(request, model, tab);
        return TAB_VIEW;
    }

    private String getFirstTemplateName() {
        final List<ContestEmailTemplate> emailTemplates =
                EmailTemplateClientUtil.listAllContestEmailTemplates();
        if (!emailTemplates.isEmpty()) {
            return emailTemplates.get(0).getType_();
        }
        return "";
    }

    @PostMapping("tab/EMAIL_TEMPLATES/update")
    public String updateEmailTemplateTabController(HttpServletRequest request, Model model,
            @ModelAttribute EmailTemplateWrapper updateEmailTemplateWrapper,
            BindingResult result, HttpServletResponse response) {
        if (!tabWrapper.getCanEdit()) {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }

        if (result.hasErrors()) {
            SetRenderParameterUtil.setErrorRenderParameter(response, "updateEmailTemplate");
            //TODO: errors
            return TAB_VIEW;
        }

        updateEmailTemplateWrapper.persist();
        SetRenderParameterUtil.addActionSuccessMessageToSession(request);
        return "redirect:" + tab.getTabUrl(updateEmailTemplateWrapper.getType());
    }

    //TODO
    //    @RequestMapping(params = "action=createEmailTemplate")
    //    public void createEmailTemplateTabController(HttpServletRequest request, Model model,
    // HttpServletResponse response) {
    //
    //        if (!tabWrapper.getCanEdit()) {
    //            SetRenderParameterUtil.setNoPermissionErrorRenderParameter(response);
    //            return;
    //        }
    //
    //        try {
    //            ContestSchedule newContestSchedule = ContestCreatorUtil.createNewSchedule();
    //            SetRenderParameterUtil
    //                    .setSuccessRenderRedirectManagerTab(response, tab.getName(),
    // newContestSchedule.getId());
    //
    //        } catch (SystemException | IOException e) {
    //            _log.warn("Create contest schedule failed with: ", e);
    //            SetRenderParameterUtil.setExceptionRenderParameter(response, e);
    //        }
    //    }
    //
    //    @RequestMapping(params = "action=deleteEmailTemplate")
    //    public void deleteEmailTemplateTabController(HttpServletRequest request, Model model,
    //            @RequestParam Long templateId,
    //            HttpServletResponse response) {
    //        if (!tabWrapper.getCanEdit()) {
    //            SetRenderParameterUtil.setNoPermissionErrorRenderParameter(response);
    //            return;
    //        }
    //        try {
    //            ContestScheduleWrapper.deleteContestSchedule(scheduleId);
    //            SetRenderParameterUtil.setSuccessRenderRedirectManagerTab(response, tab.getName
    // (), getFirstTemplateName());
    //        } catch (SystemException | PortalException | IOException e) {
    //            _log.warn("Delete contest schedule failed with: ", e);
    //            SetRenderParameterUtil.setExceptionRenderParameter(response, e);
    //        }
    //    }
}