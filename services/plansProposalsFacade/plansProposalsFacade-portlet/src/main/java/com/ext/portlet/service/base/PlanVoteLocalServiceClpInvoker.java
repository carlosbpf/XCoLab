package com.ext.portlet.service.base;

import com.ext.portlet.service.PlanVoteLocalServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class PlanVoteLocalServiceClpInvoker {
    private String _methodName0;
    private String[] _methodParameterTypes0;
    private String _methodName1;
    private String[] _methodParameterTypes1;
    private String _methodName2;
    private String[] _methodParameterTypes2;
    private String _methodName3;
    private String[] _methodParameterTypes3;
    private String _methodName4;
    private String[] _methodParameterTypes4;
    private String _methodName5;
    private String[] _methodParameterTypes5;
    private String _methodName6;
    private String[] _methodParameterTypes6;
    private String _methodName7;
    private String[] _methodParameterTypes7;
    private String _methodName8;
    private String[] _methodParameterTypes8;
    private String _methodName9;
    private String[] _methodParameterTypes9;
    private String _methodName10;
    private String[] _methodParameterTypes10;
    private String _methodName11;
    private String[] _methodParameterTypes11;
    private String _methodName12;
    private String[] _methodParameterTypes12;
    private String _methodName13;
    private String[] _methodParameterTypes13;
    private String _methodName14;
    private String[] _methodParameterTypes14;
    private String _methodName15;
    private String[] _methodParameterTypes15;
    private String _methodName526;
    private String[] _methodParameterTypes526;
    private String _methodName527;
    private String[] _methodParameterTypes527;
    private String _methodName532;
    private String[] _methodParameterTypes532;
    private String _methodName533;
    private String[] _methodParameterTypes533;
    private String _methodName534;
    private String[] _methodParameterTypes534;
    private String _methodName535;
    private String[] _methodParameterTypes535;
    private String _methodName536;
    private String[] _methodParameterTypes536;
    private String _methodName537;
    private String[] _methodParameterTypes537;
    private String _methodName538;
    private String[] _methodParameterTypes538;
    private String _methodName539;
    private String[] _methodParameterTypes539;
    private String _methodName540;
    private String[] _methodParameterTypes540;

    public PlanVoteLocalServiceClpInvoker() {
        _methodName0 = "addPlanVote";

        _methodParameterTypes0 = new String[] { "com.ext.portlet.model.PlanVote" };

        _methodName1 = "createPlanVote";

        _methodParameterTypes1 = new String[] {
                "com.ext.portlet.service.persistence.PlanVotePK"
            };

        _methodName2 = "deletePlanVote";

        _methodParameterTypes2 = new String[] {
                "com.ext.portlet.service.persistence.PlanVotePK"
            };

        _methodName3 = "deletePlanVote";

        _methodParameterTypes3 = new String[] { "com.ext.portlet.model.PlanVote" };

        _methodName4 = "dynamicQuery";

        _methodParameterTypes4 = new String[] {  };

        _methodName5 = "dynamicQuery";

        _methodParameterTypes5 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery"
            };

        _methodName6 = "dynamicQuery";

        _methodParameterTypes6 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int"
            };

        _methodName7 = "dynamicQuery";

        _methodParameterTypes7 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery", "int", "int",
                "com.liferay.portal.kernel.util.OrderByComparator"
            };

        _methodName8 = "dynamicQueryCount";

        _methodParameterTypes8 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery"
            };

        _methodName9 = "dynamicQueryCount";

        _methodParameterTypes9 = new String[] {
                "com.liferay.portal.kernel.dao.orm.DynamicQuery",
                "com.liferay.portal.kernel.dao.orm.Projection"
            };

        _methodName10 = "fetchPlanVote";

        _methodParameterTypes10 = new String[] {
                "com.ext.portlet.service.persistence.PlanVotePK"
            };

        _methodName11 = "getPlanVote";

        _methodParameterTypes11 = new String[] {
                "com.ext.portlet.service.persistence.PlanVotePK"
            };

        _methodName12 = "getPersistedModel";

        _methodParameterTypes12 = new String[] { "java.io.Serializable" };

        _methodName13 = "getPlanVotes";

        _methodParameterTypes13 = new String[] { "int", "int" };

        _methodName14 = "getPlanVotesCount";

        _methodParameterTypes14 = new String[] {  };

        _methodName15 = "updatePlanVote";

        _methodParameterTypes15 = new String[] { "com.ext.portlet.model.PlanVote" };

        _methodName526 = "getBeanIdentifier";

        _methodParameterTypes526 = new String[] {  };

        _methodName527 = "setBeanIdentifier";

        _methodParameterTypes527 = new String[] { "java.lang.String" };

        _methodName532 = "voteForPlan";

        _methodParameterTypes532 = new String[] {
                "java.lang.Long", "java.lang.Long"
            };

        _methodName533 = "unvote";

        _methodParameterTypes533 = new String[] {
                "java.lang.Long", "java.lang.Long"
            };

        _methodName534 = "getPlanVote";

        _methodParameterTypes534 = new String[] {
                "java.lang.Long", "java.lang.Long"
            };

        _methodName535 = "coutPlanVotes";

        _methodParameterTypes535 = new String[] { "java.lang.Long" };

        _methodName536 = "getPlanVotes";

        _methodParameterTypes536 = new String[] { "java.lang.Long" };

        _methodName537 = "countPlanVotes";

        _methodParameterTypes537 = new String[] { "com.ext.portlet.model.PlanType" };

        _methodName538 = "countPlanVotes";

        _methodParameterTypes538 = new String[] { "com.ext.portlet.model.Contest" };

        _methodName539 = "countPlanVotesByPlanId";

        _methodParameterTypes539 = new String[] { "java.lang.Long" };

        _methodName540 = "store";

        _methodParameterTypes540 = new String[] { "com.ext.portlet.model.PlanVote" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName0.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes0, parameterTypes)) {
            return PlanVoteLocalServiceUtil.addPlanVote((com.ext.portlet.model.PlanVote) arguments[0]);
        }

        if (_methodName1.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes1, parameterTypes)) {
            return PlanVoteLocalServiceUtil.createPlanVote((com.ext.portlet.service.persistence.PlanVotePK) arguments[0]);
        }

        if (_methodName2.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes2, parameterTypes)) {
            return PlanVoteLocalServiceUtil.deletePlanVote((com.ext.portlet.service.persistence.PlanVotePK) arguments[0]);
        }

        if (_methodName3.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes3, parameterTypes)) {
            return PlanVoteLocalServiceUtil.deletePlanVote((com.ext.portlet.model.PlanVote) arguments[0]);
        }

        if (_methodName4.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes4, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQuery();
        }

        if (_methodName5.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes5, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName6.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes6, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue());
        }

        if (_methodName7.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes7, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue(),
                (com.liferay.portal.kernel.util.OrderByComparator) arguments[3]);
        }

        if (_methodName8.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes8, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName9.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes9, parameterTypes)) {
            return PlanVoteLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                (com.liferay.portal.kernel.dao.orm.Projection) arguments[1]);
        }

        if (_methodName10.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes10, parameterTypes)) {
            return PlanVoteLocalServiceUtil.fetchPlanVote((com.ext.portlet.service.persistence.PlanVotePK) arguments[0]);
        }

        if (_methodName11.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes11, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPlanVote((com.ext.portlet.service.persistence.PlanVotePK) arguments[0]);
        }

        if (_methodName12.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes12, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPersistedModel((java.io.Serializable) arguments[0]);
        }

        if (_methodName13.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes13, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPlanVotes(((Integer) arguments[0]).intValue(),
                ((Integer) arguments[1]).intValue());
        }

        if (_methodName14.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes14, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPlanVotesCount();
        }

        if (_methodName15.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes15, parameterTypes)) {
            return PlanVoteLocalServiceUtil.updatePlanVote((com.ext.portlet.model.PlanVote) arguments[0]);
        }

        if (_methodName526.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes526, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getBeanIdentifier();
        }

        if (_methodName527.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes527, parameterTypes)) {
            PlanVoteLocalServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        if (_methodName532.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes532, parameterTypes)) {
            return PlanVoteLocalServiceUtil.voteForPlan((java.lang.Long) arguments[0],
                (java.lang.Long) arguments[1]);
        }

        if (_methodName533.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes533, parameterTypes)) {
            return PlanVoteLocalServiceUtil.unvote((java.lang.Long) arguments[0],
                (java.lang.Long) arguments[1]);
        }

        if (_methodName534.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes534, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPlanVote((java.lang.Long) arguments[0],
                (java.lang.Long) arguments[1]);
        }

        if (_methodName535.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes535, parameterTypes)) {
            return PlanVoteLocalServiceUtil.coutPlanVotes((java.lang.Long) arguments[0]);
        }

        if (_methodName536.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes536, parameterTypes)) {
            return PlanVoteLocalServiceUtil.getPlanVotes((java.lang.Long) arguments[0]);
        }

        if (_methodName537.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes537, parameterTypes)) {
            return PlanVoteLocalServiceUtil.countPlanVotes((com.ext.portlet.model.PlanType) arguments[0]);
        }

        if (_methodName538.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes538, parameterTypes)) {
            return PlanVoteLocalServiceUtil.countPlanVotes((com.ext.portlet.model.Contest) arguments[0]);
        }

        if (_methodName539.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes539, parameterTypes)) {
            return PlanVoteLocalServiceUtil.countPlanVotesByPlanId((java.lang.Long) arguments[0]);
        }

        if (_methodName540.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes540, parameterTypes)) {
            PlanVoteLocalServiceUtil.store((com.ext.portlet.model.PlanVote) arguments[0]);

            return null;
        }

        throw new UnsupportedOperationException();
    }
}
