package org.xcolab.view.pages.contestmanagement.utils;

import org.xcolab.view.pages.contestmanagement.entities.ContestMassActions;

public final class MassActionUtil {

    private MassActionUtil() { }

    public static String getSelectedMassActionTitle(long selectedMassAction) {
        String selectedMassActionTitle = "";
        Long selectedMassActionAbsolute = Math.abs(selectedMassAction);
        for (ContestMassActions contestMassAction : ContestMassActions.values()) {
            if (selectedMassActionAbsolute == contestMassAction.ordinal()) {
                if (selectedMassAction < 0) {
                    selectedMassActionTitle = contestMassAction.getReverseActionDisplayName();
                } else {
                    selectedMassActionTitle = contestMassAction.getActionDisplayName();
                }
                break;
            }
        }
        return selectedMassActionTitle;
    }
}
