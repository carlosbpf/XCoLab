package org.xcolab.view.pages.proposals.view;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.proposals.pojo.Proposal;
import org.xcolab.util.exceptions.InternalException;
import org.xcolab.view.auth.MemberAuthUtil;
import org.xcolab.view.pages.proposals.utils.ProposalPickerFilterUtil;
import org.xcolab.view.pages.proposals.utils.ProposalPickerSortingUtil;
import org.xcolab.view.pages.proposals.utils.context.ProposalsContext;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
//-- @RequestMapping("view")
public class ProposalPickerJSONController {

	private static final int MAX_CHARS_FOR_NAMES = 75;
	private static final Logger _log = LoggerFactory.getLogger(ProposalPickerJSONController.class);

	private final ProposalsContext proposalsContext;

	@Autowired
	public ProposalPickerJSONController(ProposalsContext proposalsContext) {
		this.proposalsContext = proposalsContext;
	}

	//-- @ResourceMapping("proposalPicker")
	@GetMapping("/proposals/proposalPicker")
	public void proposalPicker(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String requestType,
			@RequestParam(value = "filterKey", required = false) String filterType,
			@RequestParam(required = false) String filterText,
			@RequestParam(required = false) int start,
			@RequestParam(required = false) int end,
			@RequestParam(required = false) String sortOrder,
			@RequestParam(required = false) String sortColumn,
			@RequestParam(required = false) Long sectionId,
			@RequestParam(required = false) long contestPK) throws IOException {

		List<Pair<Proposal, Date>> proposals;
		final long memberId = MemberAuthUtil.getMemberId(request);

		switch (requestType.toUpperCase()) {
			case "SUBSCRIPTIONSANDSUPPORTING":
				proposals = ProposalPickerFilterUtil.getFilteredSubscribedSupportingProposalsForUser(
						memberId, filterType, sectionId, request, proposalsContext);
				break;
			case "SUBSCRIPTIONS":
				proposals = ProposalPickerFilterUtil.getFilteredSubscribedProposalsForUser(
						memberId, filterType, sectionId, request, proposalsContext);
				break;
			case "SUPPORTING":
				proposals = ProposalPickerFilterUtil.getFilteredSupportingProposalsForUser(
						memberId, filterType, sectionId, request, proposalsContext);
				break;
			case "ALL":
			case "CONTESTS":
				proposals = ProposalPickerFilterUtil.getFilteredAllProposals(filterText, filterType,
						sectionId, contestPK, request, proposalsContext);
				break;
			default:
				_log.error("Proposal picker was loaded with unknown requestType {}", requestType);
				throw new InternalException("Unknown requestType " + requestType);
		}

		int totalCount;
		if (proposals != null) {
			//Pushed down to Microservices
			//if (filterText != null && !filterText.isEmpty()) {
				//ProposalPickerFilter.TEXT_BASED.filter(proposals, filterText);
			//}
			totalCount = proposals.size();

			ProposalPickerSortingUtil.sortProposalsList(sortOrder, sortColumn, proposals);
			if (end >= totalCount && totalCount > 0) {
				end = totalCount;
			}
			if (totalCount > (end - start)) {
				proposals = proposals.subList(start, end);
			}

		} else {
			totalCount = 0;
			_log.error(
					"Could not retrieve proposals: proposals variable should not be null for valid"
							+ " filterKeys.(filterKey was {})",
					filterType);
		}
		response.getOutputStream().write(
				getJSONObjectMapping(proposals, totalCount).getBytes());
	}

	//-- @ResourceMapping("proposalPickerContests")
	public void proposalPickerContests(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "type", required = false) String requestType,
			@RequestParam(value = "filterKey", required = false) String filterType,
			@RequestParam(required = false) String filterText,
			@RequestParam(required = false) int start,
			@RequestParam(required = false) int end,
			@RequestParam(required = false) String sortOrder,
			@RequestParam(required = false, value = "contestSortColumn") String sortColumn,
			@RequestParam(required = false) Long sectionId) throws IOException {

		List<Pair<Contest, Date>> contests = ProposalPickerFilterUtil.getTextFilteredContests(sectionId, filterText);
		//List<Pair<ContestWrapper, Date>> contests = ProposalPickerFilterUtil.getAllContests();
		Map<Long, String> removedContests = new HashMap<>();

		/*
		TODO: Removed since not needed
		Map<Long, String> removedContests = ProposalPickerFilterUtil.filterContests(
				new ArrayList<>(contests), sectionId, request, proposalsContext, true);

		if (filterText != null && !filterText.isEmpty()) {
			ProposalPickerFilter.TEXT_BASED.filterContests(contests,
					filterText);
		}
		*/
		int totalCount = contests.size();

		if (end >= contests.size() && !contests.isEmpty()) {
			end = contests.size();
		}
		ProposalPickerSortingUtil.sortContestsList(sortOrder, sortColumn, contests, removedContests);
		if (contests.size() > (end - start)) {
			contests = contests.subList(start, end);
		}

		response.getOutputStream().write(
				getJSONObjectMappingContests(contests, totalCount, removedContests).getBytes());
	}

	/**
	 * This method is used to fill the counting bubbles for each tab
	 */
	//-- @ResourceMapping("proposalPickerCounter")
	public void proposalPickerCounter(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
				/*
		TODO: Removed to increase performance
		String filterType = request.getParameter("filterKey");
		long sectionId = Long.parseLong(request.getParameter("sectionId"));
		long userId = MemberAuthUtil.getMemberId(request);
		int numberOfSubscriptions = ProposalPickerFilterUtil.getFilteredSubscribedProposalsForUser(
				userId, filterType, sectionId, request, proposalsContext).size();
		int numberOfSupporting = ProposalPickerFilterUtil.getFilteredSupportingProposalsForUser(userId,
				filterType, sectionId, request, proposalsContext).size();
		int numberOfProposals = ProposalPickerFilterUtil.getFilteredAllProposals(filterType,
				sectionId, 0L, request, proposalsContext).size();
		int numberOfSubscriptionsSupporting = ProposalPickerFilterUtil.getFilteredSubscribedSupportingProposalsForUser(
				userId, filterType, sectionId, request, proposalsContext).size();
		int numberOfContests = ProposalPickerFilterUtil.getFilteredContests(sectionId, request, proposalsContext).size();


				JSONObject wrapper = JSONFactoryUtil.createJSONObject();
		wrapper.put("numberOfSubscriptions", numberOfSubscriptions);
		wrapper.put("numberOfSupporting", numberOfSupporting);
		wrapper.put("numberOfProposals", numberOfProposals);
		wrapper.put("numberOfSubscriptionsSupporting",
				numberOfSubscriptionsSupporting);
		wrapper.put("numberOfContests", numberOfContests);
		response.getPortletOutputStream().write(wrapper.toString().getBytes());
		*/
	}

	private String getJSONObjectMapping(List<Pair<Proposal, Date>> proposals,
										int totalNumberOfProposals) {
		JSONObject wrapper = new JSONObject();
		JSONArray proposalsJSON = new JSONArray();

		for (Pair<Proposal, Date> p : proposals) {
			Proposal wrappedProposal = new Proposal(p.getLeft());
			JSONObject o = new JSONObject();
			o.put("id", p.getLeft().getProposalId());
			o.put("proposalName", StringUtils.abbreviate(
					StringEscapeUtils.unescapeXml(wrappedProposal.getName()), MAX_CHARS_FOR_NAMES));
			o.put("contestName", StringUtils.abbreviate(wrappedProposal
					.getContest().getContestShortName(), MAX_CHARS_FOR_NAMES));
			o.put("linkUrl", wrappedProposal.getWrapped().getProposalLinkUrl(wrappedProposal.getContest()));
			o.put("contestId", wrappedProposal.getContest().getContestPK());
			if (StringUtils.isNotBlank(wrappedProposal.getTeam())) {
				o.put("team", wrappedProposal.getTeam());
			}
			o.put("authorName", wrappedProposal.getAuthorName());
			o.put("authorId", wrappedProposal.getAuthorId());
			o.put("dateSubscribed", p.getRight().getTime());
			o.put("commentsCount", wrappedProposal.getCommentsCount());
			o.put("supportersCount", wrappedProposal.getSupportersCount());
			o.put("pitch", wrappedProposal.getPitch());
			o.put("ribbon", wrappedProposal.getRibbonWrapper().getRibbon());
			o.put("ribbonText", wrappedProposal.getRibbonWrapper().getRibbonText());
			o.put("featured", wrappedProposal.isFeatured());

			JSONArray proposalContests = new JSONArray();
			// for now there can be only one contest
			JSONObject contest = new JSONObject();
			contest.put("name", wrappedProposal.getContest()
					.getContestShortName());
			contest.put("id", wrappedProposal.getContest().getContestPK());
			contest.put("contestLogoId", wrappedProposal.getContest()
					.getContestLogoId());
			proposalContests.put(contest);

			o.put("contests", proposalContests);
			proposalsJSON.put(o);
		}

		wrapper.put("proposals", proposalsJSON);
		wrapper.put("totalCount", totalNumberOfProposals);
		return wrapper.toString();
	}

	private String getJSONObjectMappingContests(
			List<Pair<Contest, Date>> contests, int totalNumberOfContests, Map<Long, String> removedContests) {
		JSONObject wrapper = new JSONObject();
		JSONArray proposalsJSON = new JSONArray();

		for (Pair<Contest, Date> p : contests) {
			Contest wrapped = p.getLeft();
			final long contestPK = wrapped.getContestPK();
			JSONObject o = new JSONObject();

			o.put("id", contestPK);
			o.put("contestShortName", StringUtils.abbreviate(
					wrapped.getContestShortName(), MAX_CHARS_FOR_NAMES));
			o.put("contestName", StringUtils.abbreviate(wrapped.getContestName(), MAX_CHARS_FOR_NAMES));
			o.put("contestPK", contestPK);
			o.put("flagText", wrapped.getFlagText());
			o.put("flag", wrapped.getFlag());
			o.put("flagTooltip", wrapped.getFlagTooltip());
			o.put("proposalsCount", wrapped.getProposalsCount());
			o.put("totalCommentsCount", wrapped.getTotalCommentsCount());
			o.put("what", wrapped.getWhatName());
			o.put("who", wrapped.getWhoName());
			o.put("where", wrapped.getWhereName());
			o.put("date", p.getRight().getTime());
			if (removedContests.containsKey(contestPK)) {
				o.put("wasFiltered", true);
				final String filterReason = removedContests.get(contestPK);
				o.put("filterReasonFocusArea", filterReason.equals(ProposalPickerFilterUtil.CONTEST_FILTER_REASON_FOCUS_AREA));
				o.put("filterReasonTier", filterReason.equals(ProposalPickerFilterUtil.CONTEST_FILTER_REASON_TIER));
			}
			proposalsJSON.put(o);
		}

		wrapper.put("contests", proposalsJSON);
		wrapper.put("totalCount", totalNumberOfContests);
		return wrapper.toString();
	}
}
