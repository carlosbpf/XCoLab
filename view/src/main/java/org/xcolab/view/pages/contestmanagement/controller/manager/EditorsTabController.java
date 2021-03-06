package org.xcolab.view.pages.contestmanagement.controller.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.view.errors.ErrorText;
import org.xcolab.view.pages.contestmanagement.entities.ContestManagerTabs;
import org.xcolab.view.taglibs.xcolab.wrapper.TabWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin/contest/manager")
public class EditorsTabController extends AbstractTabController {

    static final private ContestManagerTabs tab = ContestManagerTabs.EDITORS;

    static final private String TAB_VIEW = "contestmanagement/manager/editorsTab";

    @ModelAttribute("currentTabWrapped")
    @Override
    public TabWrapper populateCurrentTabWrapped(HttpServletRequest request) {
        tabWrapper = new TabWrapper(tab, request, tabContext);
        request.getSession().setAttribute("tabWrapper", tabWrapper);
        return tabWrapper;
    }

    @GetMapping("tab/EDITORS")
    public String showEditorsTabController(HttpServletRequest request, HttpServletResponse response,
            Model model) {
        if (!tabWrapper.getCanView()) {
            return ErrorText.ACCESS_DENIED.flashAndReturnView(request);
        }
        setPageAttributes(request, model, tab);
        return TAB_VIEW;
    }
}
