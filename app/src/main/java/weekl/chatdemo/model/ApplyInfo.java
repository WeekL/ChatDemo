package weekl.chatdemo.model;

import weekl.chatdemo.R;

public class ApplyInfo {
    public int avatarId;
    public String name;
    public String reason;
    private Handle handle;

    public ApplyInfo() {
        super();
        avatarId = R.mipmap.app_icon;
    }

    public ApplyInfo(String name, String reason){
        this();
        this.name = name;
        this.reason = reason;
        handle = Handle.NONE;
    }

    public Handle getHandle() {
        return handle;
    }

    public void setHandle (Handle handle){
        this.handle = handle;
    }

    public static enum Handle {
        NONE,
        ACCEPT,
        REFUSED
    }
}
