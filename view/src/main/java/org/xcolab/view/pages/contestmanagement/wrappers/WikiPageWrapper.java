package org.xcolab.view.pages.contestmanagement.wrappers;

import org.xcolab.client.contents.ContentsClient;
import org.xcolab.client.contents.exceptions.ContentNotFoundException;
import org.xcolab.client.contents.pojo.ContentArticle;
import org.xcolab.client.contents.pojo.ContentArticleVersion;
import org.xcolab.client.contents.pojo.ContentFolder;
import org.xcolab.client.contest.ContestClientUtil;
import org.xcolab.client.contest.pojo.Contest;
import org.xcolab.client.contest.pojo.ContestType;
import org.xcolab.util.exceptions.ReferenceResolutionException;
import org.xcolab.view.pages.contestmanagement.beans.ContestResourcesBean;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class WikiPageWrapper {

    private final Contest contest;
    private final Long loggedInUserId;
    private ContentArticle contentArticle;
    private ContentArticleVersion contentArticleVersion;

    public WikiPageWrapper(Contest contest, Long loggedInUserId) {
        this.contest = contest;
        this.loggedInUserId = loggedInUserId;
        initWikiPage();
    }

    private void initWikiPage() {

        if (contest.getResourceArticleId() != null && contest.getResourceArticleId() > 0) {
            try {
                contentArticle = ContentsClient
                        .getContentArticle(contest.getResourceArticleId());
                contentArticleVersion =
                        ContentsClient
                                .getContentArticleVersion(contentArticle.getMaxVersionId());
            } catch (ContentNotFoundException e) {
                throw ReferenceResolutionException
                        .toObject(ContentArticle.class, contest.getResourceArticleId())
                        .fromObject(Contest.class, contest.getContestPK());
            }
        } else {
            contentArticleVersion = new ContentArticleVersion();
            contentArticleVersion.setFolderId(ContentFolder.RESOURCE_FOLDER_ID);
            contentArticleVersion.setAuthorId(loggedInUserId);
            contentArticleVersion.setTitle(contest.getContestShortName());
            contentArticleVersion.setContent("");
            contentArticleVersion = ContentsClient
                    .createContentArticleVersion(contentArticleVersion);

            try {
                contentArticle = ContentsClient.getContentArticle(
                        contentArticleVersion.getContentArticleId());

                final long resourceArticleId = contentArticle.getContentArticleId();
                contest.setResourceArticleId(resourceArticleId);
                ContestClientUtil.updateContest(contest);

            } catch (ContentNotFoundException e) {
                throw new IllegalStateException(
                        "Could not retrieve ContentArticle after creation: " + contentArticle);
            }
        }

    }

    public static void updateContestWiki(Contest contest) {
        try {
            final ContentArticleVersion resourceArticleVersion = ContentsClient
                    .getLatestContentArticleVersion(contest.getResourceArticleId());
            resourceArticleVersion.setTitle(contest.getContestShortName());
            ContentsClient.updateContentArticleVersion(resourceArticleVersion);
        } catch (ContentNotFoundException ignored) {
        }
    }

    public ContestResourcesBean getContestResourcesBean() {
        final ContestType contestType =
                ContestClientUtil.getContestType(contest.getContestTypeId());
        ContestResourcesBean contestResourcesBean = new ContestResourcesBean(contestType);
        String resourcesContent = contentArticleVersion.getContent();
        contestResourcesBean.fillSectionsWithContent(resourcesContent);
        return contestResourcesBean;
    }

    public void updateWikiPage(ContestResourcesBean updatedContestResourcesBean)
            throws UnsupportedEncodingException, ParseException {
        updatedContestResourcesBean.fillOverviewSectionContent(contest);
        String updatedResourcesContent = updatedContestResourcesBean.getSectionsAsHtml();
        if (!contentArticleVersion.getContent().equals(updatedResourcesContent)) {
            contentArticleVersion.setTitle(contest.getContestShortName());
            contentArticleVersion.setContent(updatedResourcesContent);
            contentArticleVersion.setContentArticleId(contentArticle.getContentArticleId());
            contentArticleVersion.setAuthorId(loggedInUserId);
            ContentsClient.updateContentArticleVersion(contentArticleVersion);
        }
    }
}
