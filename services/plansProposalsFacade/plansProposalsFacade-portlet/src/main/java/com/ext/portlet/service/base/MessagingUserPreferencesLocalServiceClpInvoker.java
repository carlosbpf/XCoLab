package com.ext.portlet.service.base;

import com.ext.portlet.service.MessagingUserPreferencesLocalServiceUtil;

import java.util.Arrays;

/**
 * @author Brian Wing Shun Chan
 * @generated
 */
public class MessagingUserPreferencesLocalServiceClpInvoker {
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

    public MessagingUserPreferencesLocalServiceClpInvoker() {
        _methodName0 = "addMessagingUserPreferences";

        _methodParameterTypes0 = new String[] {
                "com.ext.portlet.model.MessagingUserPreferences"
            };

        _methodName1 = "createMessagingUserPreferences";

        _methodParameterTypes1 = new String[] { "long" };

        _methodName2 = "deleteMessagingUserPreferences";

        _methodParameterTypes2 = new String[] { "long" };

        _methodName3 = "deleteMessagingUserPreferences";

        _methodParameterTypes3 = new String[] {
                "com.ext.portlet.model.MessagingUserPreferences"
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

        _methodName10 = "fetchMessagingUserPreferences";

        _methodParameterTypes10 = new String[] { "long" };

        _methodName11 = "getMessagingUserPreferences";

        _methodParameterTypes11 = new String[] { "long" };

        _methodName12 = "getPersistedModel";

        _methodParameterTypes12 = new String[] { "java.io.Serializable" };

        _methodName13 = "getMessagingUserPreferenceses";

        _methodParameterTypes13 = new String[] { "int", "int" };

        _methodName14 = "getMessagingUserPreferencesesCount";

        _methodParameterTypes14 = new String[] {  };

        _methodName15 = "updateMessagingUserPreferences";

        _methodParameterTypes15 = new String[] {
                "com.ext.portlet.model.MessagingUserPreferences"
            };

        _methodName526 = "getBeanIdentifier";

        _methodParameterTypes526 = new String[] {  };

        _methodName527 = "setBeanIdentifier";

        _methodParameterTypes527 = new String[] { "java.lang.String" };

        _methodName532 = "findByUser";

        _methodParameterTypes532 = new String[] { "long" };
    }

    public Object invokeMethod(String name, String[] parameterTypes,
        Object[] arguments) throws Throwable {
        if (_methodName0.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes0, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.addMessagingUserPreferences((com.ext.portlet.model.MessagingUserPreferences) arguments[0]);
        }

        if (_methodName1.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes1, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.createMessagingUserPreferences(((Long) arguments[0]).longValue());
        }

        if (_methodName2.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes2, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.deleteMessagingUserPreferences(((Long) arguments[0]).longValue());
        }

        if (_methodName3.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes3, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.deleteMessagingUserPreferences((com.ext.portlet.model.MessagingUserPreferences) arguments[0]);
        }

        if (_methodName4.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes4, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQuery();
        }

        if (_methodName5.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes5, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName6.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes6, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue());
        }

        if (_methodName7.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes7, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQuery((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                ((Integer) arguments[1]).intValue(),
                ((Integer) arguments[2]).intValue(),
                (com.liferay.portal.kernel.util.OrderByComparator) arguments[3]);
        }

        if (_methodName8.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes8, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0]);
        }

        if (_methodName9.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes9, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.dynamicQueryCount((com.liferay.portal.kernel.dao.orm.DynamicQuery) arguments[0],
                (com.liferay.portal.kernel.dao.orm.Projection) arguments[1]);
        }

        if (_methodName10.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes10, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.fetchMessagingUserPreferences(((Long) arguments[0]).longValue());
        }

        if (_methodName11.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes11, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.getMessagingUserPreferences(((Long) arguments[0]).longValue());
        }

        if (_methodName12.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes12, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.getPersistedModel((java.io.Serializable) arguments[0]);
        }

        if (_methodName13.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes13, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.getMessagingUserPreferenceses(((Integer) arguments[0]).intValue(),
                ((Integer) arguments[1]).intValue());
        }

        if (_methodName14.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes14, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.getMessagingUserPreferencesesCount();
        }

        if (_methodName15.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes15, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.updateMessagingUserPreferences((com.ext.portlet.model.MessagingUserPreferences) arguments[0]);
        }

        if (_methodName526.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes526, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.getBeanIdentifier();
        }

        if (_methodName527.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes527, parameterTypes)) {
            MessagingUserPreferencesLocalServiceUtil.setBeanIdentifier((java.lang.String) arguments[0]);

            return null;
        }

        if (_methodName532.equals(name) &&
                Arrays.deepEquals(_methodParameterTypes532, parameterTypes)) {
            return MessagingUserPreferencesLocalServiceUtil.findByUser(((Long) arguments[0]).longValue());
        }

        throw new UnsupportedOperationException();
    }
}
