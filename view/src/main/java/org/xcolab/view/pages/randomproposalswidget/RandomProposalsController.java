package org.xcolab.view.pages.randomproposalswidget;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.proposals.ProposalClientUtil;
import org.xcolab.client.proposals.exceptions.ProposalNotFoundException;
import org.xcolab.client.proposals.pojo.Proposal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class RandomProposalsController {

    @GetMapping("/randomproposalswidget")
    public String showRandomProposals(HttpServletRequest request, HttpServletResponse response, Model model) {

        RandomProposalsPreferences preferences = new RandomProposalsPreferences();


        ProposalsModel proposalsModel = new ProposalsModel(getProposals(preferences), preferences
    			, ConfigurationAttributeKey.COLAB_URL+ "/proposal?img_id=");

    	model.addAttribute("proposalsModel", proposalsModel);

    	return "/randomproposalswidget/showProposals";
    }

    private List<Proposal> getProposals(RandomProposalsPreferences preferences) {

        List<Proposal> ret = new ArrayList<>();
		List<Proposal> proposals = getAvailableProposals(preferences);

        //TODO LR: remove loop and use micro service pojo
        if (proposals != null) {
            Collections.shuffle(proposals);
            for (int i = 0; i < proposals.size() && i < preferences.getFeedSize(); ++i) {
                try {
                    ret.add((
                            ProposalClientUtil.getProposal(proposals.get(i).getProposalId())));
                } catch (ProposalNotFoundException  e) {
                    //ignored for now, will be removed after LR
                }
            }
        }

		return ret;
	}

	private List<Proposal> getAvailableProposals(RandomProposalsPreferences preferences) {
        List<Proposal> availableProposals = new ArrayList<>();
        Long[] selectedPhases = preferences.getSelectedPhases();
        if (selectedPhases == null) {
            return null;
        }
        Long[] flagFilters = preferences.getFlagFilters();

        for (Long contestPhaseId : selectedPhases) {
            if (flagFilters == null || flagFilters.length == 0) {
                availableProposals
                        .addAll(ProposalClientUtil.listProposals(0, Integer.MAX_VALUE, null, true,
                        contestPhaseId, null));
            } else {
                for (Long flagFilter : flagFilters) {
                    availableProposals
                            .addAll(ProposalClientUtil
                                    .listProposals(0, Integer.MAX_VALUE, null, true,
                            contestPhaseId, flagFilter.intValue()));
                }
            }
        }
        return availableProposals;
    }
}