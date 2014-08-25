package com.ext.portlet.service.base;

import com.ext.portlet.service.PointDistributionTargetLocalServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class PointDistributionTargetLocalServiceClpInvoker {
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
    private String _methodName576;
    private String[] _methodParameterTypes576;
    private String _methodName577;
    private String[] _methodParameterTypes577;
    private String _methodName582;
    private String[] _methodParameterTypes582;

    public PointDistributionTargetLocalServiceClpInvoker() {
        _methodName0 = "addPointDistributionTarget";

        _methodParameterTypes0 = new String[] {
                "com.ext.portlet.model.PointDistributionTarget"
            };

        _methodName1 = "createPointDistributionTarget";

        _methodParameterTypes1 = new String[] { "long" };

        _methodName2 = "deletePointDistributionTarget";

        _methodParameterTypes2 = new String[] { "long" };

        _methodName3 = "deletePointDistributionTarget";

        _methodParameterTypes3 = new String[] {
                "com.ext.portlet.model.PointDistributionTarget"
            };

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

        _methodName10 = "fetchPointDistributionTarget";

        _methodParameterTypes10 = new String[] { "long" };

        _methodName11 = "getPointDistributionTarget";

        _methodParameterTypes11 = new String[] { "long" };

        _methodName12 = "getPersistedModel";

        _methodParameterTypes12 = new String[] { "java.io.Serializable" };

        _methodName13 = "getPointDistributionTargets";

        _methodParameterTypes13 = new String[] { "int", "int" };

        _methodName14 = "getPointDistributionTargetsCount";

        _methodParameterTypes14 = new String[] {  };

        _methodName15 = "updatePointDistributionTarget";

        _methodParameterTypes15 = new String[] {
                "com.ext.portlet.model.PointDistributionTarget"
            };

        _methodName576 = "getBeanIdentifier";

        _methodParameterTypes576 = new String[] {  };

        _methodName577 = "setBeanIdentifier";

        _methodParameterTypes577 = new String[] { "java.lang.String" };

        _methodName582 = "findByContestId";

        _methodParameterTypes582 = new String[] { "long" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName0.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes0, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.addPointDistributionTarget((com.ext.portlet.model.PointDistributionTarget) arguments[0]);
        }

        if (_methodName1.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes1, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.createPointDistributionTarget(((Long) arguments[0]).longValue());
        }

        if (_methodName2.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes2, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.deletePointDistributionTarget(((Long) arguments[0]).longValue());
        }

        if (_methodName3.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes3, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.deletePointDistributionTarget((com.ext.portlet.model.PointDistributionTarget) arguments[0]);
        }

        if (_methodName4.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes4, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQuery();
        }

        if (_methodName5.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes5, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName6.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes6, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue());
        }

        if (_methodName7.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes7, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue(),
                (com.liferay.portal.kernel.util.OrderByComparator) arguments[3]);
        }

        if (_methodName8.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes8, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName9.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes9, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                (com.liferay.portal.kernel.dao.orm.Projection) arguments[1]);
        }

        if (_methodName10.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes10, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.fetchPointDistributionTarget(((Long) arguments[0]).longValue());
        }

        if (_methodName11.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes11, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.getPointDistributionTarget(((Long) arguments[0]).longValue());
        }

        if (_methodName12.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes12, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.getPersistedModel((java.io.Serializable) arguments[0]);
        }

        if (_methodName13.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes13, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.getPointDistributionTargets(((Integer) arguments[0]).intValue(),
                ((Integer) arguments[1]).intValue());
        }

        if (_methodName14.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes14, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.getPointDistributionTargetsCount();
        }

        if (_methodName15.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes15, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.updatePointDistributionTarget((com.ext.portlet.model.PointDistributionTarget) arguments[0]);
        }

        if (_methodName576.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes576, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.getBeanIdentifier();
        }

        if (_methodName577.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes577, parameterTypes)) {
            PointDistributionTargetLocalServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        if (_methodName582.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes582, parameterTypes)) {
            return PointDistributionTargetLocalServiceUtil.findByContestId(((Long) arguments[0]).longValue());
        }

        throw new UnsupportedOperationException();
    }
}