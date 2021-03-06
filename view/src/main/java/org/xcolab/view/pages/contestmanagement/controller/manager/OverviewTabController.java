package org.xcolab.view.pages.contestmanagement.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.entity.utils.flash.AlertMessage;
import org.xcolab.view.errors.ErrorText;
import org.xcolab.view.pages.contestmanagement.entities.ContestManagerTabs;
import org.xcolab.view.pages.contestmanagement.entities.ContestMassActions;
import org.xcolab.view.pages.contestmanagement.entities.LabelValue;
import org.xcolab.view.pages.contestmanagement.entities.MassActionRequiresConfirmationException;
import org.xcolab.view.pages.contestmanagement.wrappers.ContestOverviewWrapper;
import org.xcolab.view.pages.contestmanagement.wrappers.MassActionConfirmationWrapper;
import org.xcolab.view.taglibs.xcolab.wrapper.TabWrapper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/contest")
public class OverviewTabController extends AbstractTabController {

    static final private ContestManagerTabs tab = ContestManagerTabs.OVERVIEW;
    static final private String TAB_VIEW = "contestmanagement/manager/overviewTab";

    static final private String CONFIRM_VIEW_PATH =
            "contestmanagement/manager/massActionConfirmation/";

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(HttpServletRequest request) {
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @ModelAttribute("massActionsItems")
    public List<LabelValue> populateMassActionsItems(HttpServletRequest request) {
        List<LabelValue> contestMassActionItems = new ArrayList<>();

        for (ContestMassActions contestMassAction : ContestMassActions.values()) {
            contestMassActionItems
                    .add(new LabelValue((long) contestMassAction.ordinal(),
                            contestMassAction.getActionDisplayName()));
            if (contestMassAction.getHasReverseAction()) {
                contestMassActionItems.add(new LabelValue((long) -contestMassAction.ordinal(),
                        contestMassAction.getReverseActionDisplayName()));
            }
        }

        return contestMassActionItems;
    }

    @GetMapping({"", "manager"})
    public String showAdminTabController(HttpServletRequest request, HttpServletResponse response,
            Model model) {
        if (!tabWrapper.getCanView()) {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }
        setPageAttributes(request, model, tab);
        model.addAttribute("contestOverviewWrapper", new ContestOverviewWrapper(request));
        return TAB_VIEW;
    }

    @PostMapping("manager/update")
    public String updateContestOverviewTabController(HttpServletRequest request, Model model,
            @ModelAttribute ContestOverviewWrapper updateContestOverviewWrapper,
            HttpServletResponse response)
            throws IOException, InvocationTargetException, IllegalAccessException {
        if (!tabWrapper.getCanEdit()) {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }
        try {
            updateContestOverviewWrapper.executeMassAction(request, response);
            String massActionTitle = updateContestOverviewWrapper.getSelectedMassActionTitle();
            AlertMessage.CHANGES_SAVED.flash(request);
            return "redirect:/admin/contest/manager";
        } catch (InvocationTargetException e) {
            Boolean massActionRequiresConfirmation =
                    e.getTargetException() instanceof MassActionRequiresConfirmationException;
            if (massActionRequiresConfirmation) {
                final int selectedMassActionId =
                        updateContestOverviewWrapper.getSelectedMassAction().intValue();
                ContestMassActions contestMassAction = ContestMassActions.values()[selectedMassActionId];
                String confirmView = contestMassAction.getMethod().getName();
                List<Long> contestIds = updateContestOverviewWrapper.getSelectedContestIds();
                model.addAttribute("massActionConfirmationWrapper",
                        new MassActionConfirmationWrapper(contestIds, selectedMassActionId));
                model.addAttribute("massActionId", selectedMassActionId);
                return CONFIRM_VIEW_PATH + confirmView;
            } else {
                throw e;
            }
        }
    }

    @GetMapping("api/massAction")
    public void getExportController(HttpServletRequest request, Model model,
            @ModelAttribute ContestOverviewWrapper updateContestOverviewWrapper,
            HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        if (!tabWrapper.getCanEdit()) {
            return;
        }
        updateContestOverviewWrapper.executeMassAction(request, response);
    }
}