package org.xcolab.view.pages.loginregister.singlesignon;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.xcolab.client.admin.enums.ConfigurationAttributeKey;
import org.xcolab.client.members.MembersClient;
import org.xcolab.client.members.exceptions.MemberNotFoundException;
import org.xcolab.client.members.pojo.Member;
import org.xcolab.view.auth.AuthenticationContext;
import org.xcolab.view.pages.loginregister.CreateUserBean;
import org.xcolab.view.pages.loginregister.ImageUploadUtils;
import org.xcolab.view.pages.loginregister.MainViewController;
import org.xcolab.view.pages.loginregister.exception.UserLocationNotResolvableException;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/sso/google")
public class GoogleController {

    private static final String GOOGLE_OAUTH_REQUEST_STATE_TOKEN = "GOOGLE_OAUTH_REQUEST_STATE_TOKEN";

    private final AuthenticationContext authenticationContext;

    @Autowired
    public GoogleController(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @GetMapping("register")
    public void initiateOpenIdRegistration(HttpServletRequest request, Model model, HttpServletResponse HttpServletResponse)
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute(MainViewController.SSO_TARGET_KEY, MainViewController.SSO_TARGET_REGISTRATION);

        initiateOpenIdRequest(request, HttpServletResponse);
    }

    private void initiateOpenIdRequest(HttpServletRequest request, HttpServletResponse HttpServletResponse)
            throws IOException {
        HttpSession session = request.getSession();

        final String callbackUrl = ConfigurationAttributeKey.COLAB_URL.get()
                + SsoEndpoint.GOOGLE_CALLBACK.getUrl();
        GoogleAuthHelper helper = new GoogleAuthHelper(callbackUrl);
        String requestUrl = helper.buildLoginUrl();

        session.setAttribute(GOOGLE_OAUTH_REQUEST_STATE_TOKEN, helper.getStateToken());
        HttpServletResponse.sendRedirect(requestUrl);

        String referrer = request.getHeader("referer");
        session.setAttribute(MainViewController.PRE_LOGIN_REFERRER_KEY, referrer);
    }

    @GetMapping("login")
    public void initiateOpenIdLogin(HttpServletRequest request, Model model, HttpServletResponse HttpServletResponse)
            throws IOException {
        HttpSession session = request.getSession();
        session.setAttribute(MainViewController.SSO_TARGET_KEY, MainViewController.SSO_TARGET_LOGIN);

        initiateOpenIdRequest(request, HttpServletResponse);
    }

    @GetMapping("callback")
    public void readOpenIdResponse(HttpServletRequest request, Model model,
            HttpServletResponse response)
            throws IOException, JSONException {

        HttpSession session = request.getSession();
        session.removeAttribute(SSOKeys.FACEBOOK_USER_ID);

        String redirectUrl = (String) session.getAttribute(MainViewController.PRE_LOGIN_REFERRER_KEY);
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = "/";
        }
        session.removeAttribute(MainViewController.PRE_LOGIN_REFERRER_KEY);

        // Check whether the state token matches => CSRF protection
        String stateToken = request.getParameter("state");
        String authCode = request.getParameter("code");
        if (authCode != null && stateToken != null && stateToken
                .equals(session.getAttribute(GOOGLE_OAUTH_REQUEST_STATE_TOKEN))) {
            final String callbackUrl = ConfigurationAttributeKey.COLAB_URL.get()
                    + SsoEndpoint.GOOGLE_CALLBACK.getUrl();
            JSONObject userInfo = new GoogleAuthHelper(callbackUrl).getUserInfoJson(authCode);

            String country = null;

            try {
                country = getCountry(userInfo.getString("locale"));
            } catch (UserLocationNotResolvableException ignored) { }

            Member registeredMember = findRegisteredMember(userInfo, country);
            if (registeredMember != null) {
                String profilePicURL = userInfo.getString("picture");
                session.setAttribute(SSOKeys.OPEN_ID_LOGIN, registeredMember.getUserId());

                String path = session.getServletContext().getRealPath("/");
                ImageUploadUtils.updateProfilePicture(path, registeredMember, profilePicURL);
                authenticationContext.authenticate(request, registeredMember);
                MembersClient
                        .createLoginLog(registeredMember.getUserId(), request.getRemoteAddr(), redirectUrl);

                response.sendRedirect(redirectUrl);
            } else {
                registerMember(request, response, userInfo, country, redirectUrl);
            }
        } else {
            response.sendRedirect(redirectUrl);
        }
    }

    private Member findRegisteredMember(JSONObject userInfo, String country) {
        String googleId = userInfo.getString("id");
        String emailAddress = userInfo.getString("email");
        boolean verifiedEmail = userInfo.optBoolean("verified_email");

        Member member;
        try {
            member = MembersClient.findMemberByGoogleId(googleId);
        } catch (MemberNotFoundException e) {
            if (StringUtils.isNotBlank(emailAddress) && verifiedEmail) {
                try {
                    member = MembersClient.findMemberByEmailAddress(emailAddress);
                    member.setGoogleId(googleId);
                    if (StringUtils.isNotBlank(country)) {
                        member.setCountry(country);
                        MembersClient.updateMember(member);
                    }

                    MembersClient.updateMember(member);

                } catch (MemberNotFoundException e2) {
                    member = null;
                }
            } else {
                member = null;
            }
        }
        return member;
    }

    private void registerMember(HttpServletRequest request, HttpServletResponse response,
            JSONObject userInfo, String country, String redirectUrl) throws IOException {
        String googleId = userInfo.getString("id");
        String firstName = userInfo.getString("given_name").replaceAll("[^0-9a-zA-Z\\-\\_\\.]", "");
        String lastName = userInfo.getString("family_name").replaceAll("[^0-9a-zA-Z\\-\\_\\.]", "");
        String emailAddress = userInfo.getString("email");
        String profilePicURL = userInfo.getString("picture");

        HttpSession session = request.getSession();

        String path = session.getServletContext().getRealPath("/");

        session.setAttribute(SSOKeys.SSO_GOOGLE_ID, googleId);
        String screenName = null;
        if (StringUtils.isNotBlank(emailAddress)) {
            session.setAttribute(SSOKeys.SSO_EMAIL, emailAddress);
            // Screenname = email prefix until @ character
            screenName = emailAddress.substring(0, emailAddress.indexOf("@"));
            screenName = screenName.replaceAll("[^0-9a-zA-Z\\-\\_\\.]", "");
            session.setAttribute(SSOKeys.SSO_SCREEN_NAME, screenName);
        }

        if (StringUtils.isNotBlank(firstName)) {
            session.setAttribute(SSOKeys.SSO_FIRST_NAME, firstName);
        }
        if (StringUtils.isNotBlank(lastName)) {
            session.setAttribute(SSOKeys.SSO_LAST_NAME, lastName);
        }
        session.setAttribute(SSOKeys.SSO_COUNTRY, country);

        long imageId = 0;
        if (StringUtils.isNotBlank(profilePicURL)) {
            imageId = ImageUploadUtils.linkProfilePicture(path, profilePicURL);
            session.setAttribute(SSOKeys.SSO_PROFILE_IMAGE_ID, Long.toString(imageId));
        }

        if (session.getAttribute(MainViewController.SSO_TARGET_KEY)
                .equals(MainViewController.SSO_TARGET_LOGIN)) {
            response.sendRedirect(SsoEndpoint.REGISTER_OR_LOGIN.getUrl());
        } else if (session.getAttribute(MainViewController.SSO_TARGET_KEY)
                .equals(MainViewController.SSO_TARGET_REGISTRATION)) {
            // Create the user and login
            // append SSO attributes
            CreateUserBean userBean = new CreateUserBean();
            String password = RandomStringUtils.random(12, true, true);
            userBean.setPassword(password);
            userBean.setRetypePassword(password);
            userBean.setFirstName(firstName);
            userBean.setLastName(lastName);
            userBean.setEmail(emailAddress);
            userBean.setCountry(country);
            userBean.setImageId(Long.toString(imageId));

            if (StringUtils.isNotBlank(screenName)) {
                userBean.setScreenName(screenName);

                // Validate uniqueness of the screen name
                // The chance of a collision among 40 equal screennames is 50% -> 5 tries should be sufficient
                for (int i = 0; i < 5; i++) {
                    try {
                        MembersClient.findMemberByScreenName(screenName);
                        // Generate a random suffix for the non-unique screenName
                        screenName =
                                userBean.getScreenName()
                                        .concat(RandomStringUtils.random(4, false, true));
                    } catch (MemberNotFoundException e3) {
                        //screen name not used - we can continue
                        break;
                    }
                }
            }

            userBean.setScreenName(screenName);

            MainViewController.completeRegistration(request, response, userBean,
                    redirectUrl, true);
        }
    }

    private String getCountry(String localeCountryString) throws UserLocationNotResolvableException {
        return getCountryFromLocaleObject(localeCountryString);
    }

    private String getCountryFromLocaleObject(String localeCountryString) throws UserLocationNotResolvableException {
        if (StringUtils.isNotEmpty(localeCountryString)) {
            Locale locale = new Locale("en", localeCountryString);

            return locale.getDisplayCountry();
        }

        throw new UserLocationNotResolvableException("Could not retrieve country from Google locale");
    }
}