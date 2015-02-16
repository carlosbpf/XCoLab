package org.xcolab.portlets.contestmanagement.controller;

import javax.validation.Valid;
import com.ext.portlet.model.Contest;
import com.ext.portlet.model.ContestSchedule;
import com.ext.portlet.model.PlanTemplate;
import com.ext.portlet.service.ContestLocalServiceUtil;
import com.ext.portlet.service.ContestScheduleLocalServiceUtil;
import com.ext.portlet.service.PlanTemplateLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.exceptions.TabAuthorizationException;
import org.xcolab.interfaces.TabEnum;
import org.xcolab.portlets.contestmanagement.beans.ContestDescriptionBean;
import org.xcolab.portlets.contestmanagement.views.ContestDetailsTabs;
import org.xcolab.portlets.contestmanagement.utils.LabelValue;
import org.xcolab.utils.emailnotification.ContestCreationNotification;
import org.xcolab.wrapper.TabWrapper;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 2/6/2015.
 */

@Controller
@RequestMapping("view")
public class ContestDetailsDescriptionTabController extends ContestDetailsBaseTabController {

    @Autowired
    private Validator validator;
    static final private TabEnum tab = ContestDetailsTabs.DESCRIPTION;
    static final private String TAB_VIEW = "details/descriptionTab";

    @InitBinder("contestDescriptionBean")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @ModelAttribute("proposalTemplateSelectionItems")
    public List<LabelValue> populateProposalTemplateSelectionItems(){
        return getProposalTemplateSelectionItems();
    }

    @ModelAttribute("scheduleTemplateSelectionItems")
    public List<LabelValue> populateScheduleTemplateSelectionItems(){
        return getScheduleTemplateSelectionItems();
    }

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(PortletRequest request) throws PortalException, SystemException{
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getPortletSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @RequestMapping(params = "tab=DESCRIPTION")
    public String showDescriptionTabController(PortletRequest request, PortletResponse response, Model model)
            throws PortalException, SystemException {

        if(!tabWrapper.getCanView()) {
            return NO_PERMISSION_TAB_VIEW;
        }
        setPageAttributes(request, model, tab);
        model.addAttribute("contestDescriptionBean", new ContestDescriptionBean(getContest()));
        return TAB_VIEW;
    }

    @RequestMapping(params = "action=updateContestDetails")
    public void updateDescriptionTabController(ActionRequest request, Model model, ActionResponse response,
                                             @Valid ContestDescriptionBean updatedContestDescriptionBean,
                                             BindingResult result) {
        boolean createNew = false;

        if(!tabWrapper.getCanEdit()) {
            setNoPermissionErrorRenderParameter(response);
            return;
        }

        if (result.hasErrors()) {
            setErrorRenderParameter(response, "updateContestDetails");
            return;
        }

        try{
            // TODO check Input
            updatedContestDescriptionBean.persist();

            if (createNew) {
                ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
                Contest contest = ContestLocalServiceUtil.getContest(updatedContestDescriptionBean.getContestPK());
                sendEmailNotificationToAuthor(themeDisplay, contest);
            }
            setSuccessRenderRedirect(response, tab.getName());
        } catch(Exception e){
            setNotFoundErrorRenderParameter(response);
        }

    }

    @RequestMapping(params = {"action=updateContestDetails", "error=true"})
    public String reportError(PortletRequest request, Model model) throws PortalException, SystemException {
        return TAB_VIEW;
    }

    private void sendEmailNotificationToAuthor(ThemeDisplay themeDisplay, Contest contest) throws PortalException, SystemException{
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setPortalURL(themeDisplay.getPortalURL());
        new ContestCreationNotification(contest, serviceContext).sendEmailNotification();
    }

    private List<LabelValue> getScheduleTemplateSelectionItems(){
        List<LabelValue> selectItems = new ArrayList<>();
        try {
            for (ContestSchedule scheduleTemplate : ContestScheduleLocalServiceUtil.getContestSchedules(0, Integer.MAX_VALUE)) {
                if(!scheduleTemplate.isInvisible()) {
                    selectItems.add(new LabelValue(scheduleTemplate.getContestSchedulePK(), scheduleTemplate.getName()));
                }
            }
        } catch (Exception e){
        }
        return selectItems;
    }

    private List<LabelValue> getProposalTemplateSelectionItems(){
        List<LabelValue> selectItems = new ArrayList<>();
        try {
            for (PlanTemplate proposalTemplate : PlanTemplateLocalServiceUtil.getPlanTemplates(0, Integer.MAX_VALUE)) {
                selectItems.add(new LabelValue(proposalTemplate.getId(), proposalTemplate.getName()));
            }
        } catch (Exception e){
        }
        return selectItems;
    }

}