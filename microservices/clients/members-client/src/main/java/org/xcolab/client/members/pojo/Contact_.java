package org.xcolab.client.members.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.core.ParameterizedTypeReference;

import org.xcolab.util.http.client.types.TypeProvider;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact_ implements Serializable {
    public static final TypeProvider<Contact_> TYPES =
            new TypeProvider<>(Contact_.class,
                    new ParameterizedTypeReference<List<Contact_>>() {
                    });

    private static final long serialVersionUID = -48266924;

    private Long contactid;
    private Long companyid;
    private Long userid;
    private String username;
    private Timestamp createdate;
    private Timestamp modifieddate;
    private Long accountid;
    private Long parentcontactid;
    private String firstname;
    private String middlename;
    private String lastname;
    private Integer prefixid;
    private Integer suffixid;
    private Boolean male;
    private Timestamp birthday;
    private String smssn;
    private String aimsn;
    private String facebooksn;
    private String icqsn;
    private String jabbersn;
    private String msnsn;
    private String myspacesn;
    private String skypesn;
    private String twittersn;
    private String ymsn;
    private String employeestatusid;
    private String employeenumber;
    private String jobtitle;
    private String jobclass;
    private String hoursofoperation;
    private Long classnameid;
    private Long classpk;
    private String emailaddress;

    public Contact_() {
    }

    public Contact_(Contact_ value) {
        this.contactid = value.contactid;
        this.companyid = value.companyid;
        this.userid = value.userid;
        this.username = value.username;
        this.createdate = value.createdate;
        this.modifieddate = value.modifieddate;
        this.accountid = value.accountid;
        this.parentcontactid = value.parentcontactid;
        this.firstname = value.firstname;
        this.middlename = value.middlename;
        this.lastname = value.lastname;
        this.prefixid = value.prefixid;
        this.suffixid = value.suffixid;
        this.male = value.male;
        this.birthday = value.birthday;
        this.smssn = value.smssn;
        this.aimsn = value.aimsn;
        this.facebooksn = value.facebooksn;
        this.icqsn = value.icqsn;
        this.jabbersn = value.jabbersn;
        this.msnsn = value.msnsn;
        this.myspacesn = value.myspacesn;
        this.skypesn = value.skypesn;
        this.twittersn = value.twittersn;
        this.ymsn = value.ymsn;
        this.employeestatusid = value.employeestatusid;
        this.employeenumber = value.employeenumber;
        this.jobtitle = value.jobtitle;
        this.jobclass = value.jobclass;
        this.hoursofoperation = value.hoursofoperation;
        this.classnameid = value.classnameid;
        this.classpk = value.classpk;
        this.emailaddress = value.emailaddress;
    }

    public Contact_(
            Long contactid,
            Long companyid,
            Long userid,
            String username,
            Timestamp createdate,
            Timestamp modifieddate,
            Long accountid,
            Long parentcontactid,
            String firstname,
            String middlename,
            String lastname,
            Integer prefixid,
            Integer suffixid,
            Boolean male,
            Timestamp birthday,
            String smssn,
            String aimsn,
            String facebooksn,
            String icqsn,
            String jabbersn,
            String msnsn,
            String myspacesn,
            String skypesn,
            String twittersn,
            String ymsn,
            String employeestatusid,
            String employeenumber,
            String jobtitle,
            String jobclass,
            String hoursofoperation,
            Long classnameid,
            Long classpk,
            String emailaddress
    ) {
        this.contactid = contactid;
        this.companyid = companyid;
        this.userid = userid;
        this.username = username;
        this.createdate = createdate;
        this.modifieddate = modifieddate;
        this.accountid = accountid;
        this.parentcontactid = parentcontactid;
        this.firstname = firstname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.prefixid = prefixid;
        this.suffixid = suffixid;
        this.male = male;
        this.birthday = birthday;
        this.smssn = smssn;
        this.aimsn = aimsn;
        this.facebooksn = facebooksn;
        this.icqsn = icqsn;
        this.jabbersn = jabbersn;
        this.msnsn = msnsn;
        this.myspacesn = myspacesn;
        this.skypesn = skypesn;
        this.twittersn = twittersn;
        this.ymsn = ymsn;
        this.employeestatusid = employeestatusid;
        this.employeenumber = employeenumber;
        this.jobtitle = jobtitle;
        this.jobclass = jobclass;
        this.hoursofoperation = hoursofoperation;
        this.classnameid = classnameid;
        this.classpk = classpk;
        this.emailaddress = emailaddress;
    }

    public Long getContactId() {
        return this.contactid;
    }

    public void setContactId(Long contactid) {
        this.contactid = contactid;
    }

    public Long getCompanyId() {
        return this.companyid;
    }

    public void setCompanyId(Long companyid) {
        this.companyid = companyid;
    }

    public Long getUserId() {
        return this.userid;
    }

    public void setUserId(Long userid) {
        this.userid = userid;
    }

    public String getUserName() {
        return this.username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public Timestamp getCreateDate() {
        return this.createdate;
    }

    public void setCreateDate(Timestamp createdate) {
        this.createdate = createdate;
    }

    public Timestamp getModifiedDate() {
        return this.modifieddate;
    }

    public void setModifiedDate(Timestamp modifieddate) {
        this.modifieddate = modifieddate;
    }

    public Long getAccountId() {
        return this.accountid;
    }

    public void setAccountId(Long accountid) {
        this.accountid = accountid;
    }

    public Long getParentContactId() {
        return this.parentcontactid;
    }

    public void setParentContactId(Long parentcontactid) {
        this.parentcontactid = parentcontactid;
    }

    public String getFirstName() {
        return this.firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getMiddleName() {
        return this.middlename;
    }

    public void setMiddleName(String middlename) {
        this.middlename = middlename;
    }

    public String getLastName() {
        return this.lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public Integer getPrefixId() {
        return this.prefixid;
    }

    public void setPrefixId(Integer prefixid) {
        this.prefixid = prefixid;
    }

    public Integer getSuffixId() {
        return this.suffixid;
    }

    public void setSuffixId(Integer suffixid) {
        this.suffixid = suffixid;
    }

    public Boolean getMale() {
        return this.male;
    }

    public void setMale(Boolean male) {
        this.male = male;
    }

    public Timestamp getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Timestamp birthday) {
        this.birthday = birthday;
    }

    public String getSmsSn() {
        return this.smssn;
    }

    public void setSmsSn(String smssn) {
        this.smssn = smssn;
    }

    public String getAimSn() {
        return this.aimsn;
    }

    public void setAimSn(String aimsn) {
        this.aimsn = aimsn;
    }

    public String getFacebookSn() {
        return this.facebooksn;
    }

    public void setFacebookSn(String facebooksn) {
        this.facebooksn = facebooksn;
    }

    public String getIcqSn() {
        return this.icqsn;
    }

    public void setIcqSn(String icqsn) {
        this.icqsn = icqsn;
    }

    public String getJabberSn() {
        return this.jabbersn;
    }

    public void setJabberSn(String jabbersn) {
        this.jabbersn = jabbersn;
    }

    public String getMsnSn() {
        return this.msnsn;
    }

    public void setMsnSn(String msnsn) {
        this.msnsn = msnsn;
    }

    public String getMySpaceSn() {
        return this.myspacesn;
    }

    public void setMySpaceSn(String myspacesn) {
        this.myspacesn = myspacesn;
    }

    public String getSkypeSn() {
        return this.skypesn;
    }

    public void setSkypeSn(String skypesn) {
        this.skypesn = skypesn;
    }

    public String getTwitterSn() {
        return this.twittersn;
    }

    public void setTwitterSn(String twittersn) {
        this.twittersn = twittersn;
    }

    public String getYmSn() {
        return this.ymsn;
    }

    public void setYmSn(String ymsn) {
        this.ymsn = ymsn;
    }

    public String getEmployeeStatusId() {
        return this.employeestatusid;
    }

    public void setEmployeeStatusId(String employeestatusid) {
        this.employeestatusid = employeestatusid;
    }

    public String getEmployeeNumber() {
        return this.employeenumber;
    }

    public void setEmployeeNumber(String employeenumber) {
        this.employeenumber = employeenumber;
    }

    public String getJobTitle() {
        return this.jobtitle;
    }

    public void setJobTitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getJobClass() {
        return this.jobclass;
    }

    public void setJobClass(String jobclass) {
        this.jobclass = jobclass;
    }

    public String getHoursOfOperation() {
        return this.hoursofoperation;
    }

    public void setHoursOfOperation(String hoursofoperation) {
        this.hoursofoperation = hoursofoperation;
    }

    public Long getClassNameId() {
        return this.classnameid;
    }

    public void setClassNameId(Long classnameid) {
        this.classnameid = classnameid;
    }

    public Long getClassPK() {
        return this.classpk;
    }

    public void setClassPK(Long classpk) {
        this.classpk = classpk;
    }

    public String getEmailAddress() {
        return this.emailaddress;
    }

    public void setEmailAddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Contact_ (");

        sb.append(contactid);
        sb.append(", ").append(companyid);
        sb.append(", ").append(userid);
        sb.append(", ").append(username);
        sb.append(", ").append(createdate);
        sb.append(", ").append(modifieddate);
        sb.append(", ").append(accountid);
        sb.append(", ").append(parentcontactid);
        sb.append(", ").append(firstname);
        sb.append(", ").append(middlename);
        sb.append(", ").append(lastname);
        sb.append(", ").append(prefixid);
        sb.append(", ").append(suffixid);
        sb.append(", ").append(male);
        sb.append(", ").append(birthday);
        sb.append(", ").append(smssn);
        sb.append(", ").append(aimsn);
        sb.append(", ").append(facebooksn);
        sb.append(", ").append(icqsn);
        sb.append(", ").append(jabbersn);
        sb.append(", ").append(msnsn);
        sb.append(", ").append(myspacesn);
        sb.append(", ").append(skypesn);
        sb.append(", ").append(twittersn);
        sb.append(", ").append(ymsn);
        sb.append(", ").append(employeestatusid);
        sb.append(", ").append(employeenumber);
        sb.append(", ").append(jobtitle);
        sb.append(", ").append(jobclass);
        sb.append(", ").append(hoursofoperation);
        sb.append(", ").append(classnameid);
        sb.append(", ").append(classpk);
        sb.append(", ").append(emailaddress);

        sb.append(")");
        return sb.toString();
    }
}
