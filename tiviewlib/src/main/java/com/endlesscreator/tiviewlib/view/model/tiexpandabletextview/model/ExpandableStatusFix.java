package com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.model;

import com.endlesscreator.tiviewlib.view.model.tiexpandabletextview.app.StatusType;

/**
 * 为ExpandableTextView添加展开和收回状态的记录
 */
public interface ExpandableStatusFix {
    void setStatus(StatusType status);

    StatusType getStatus();
}
