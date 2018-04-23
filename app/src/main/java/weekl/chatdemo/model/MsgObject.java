package weekl.chatdemo.model;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;


import weekl.chatdemo.util.DateUtil;

public class MsgObject {
    public static final String NOT_SHOW = "不显示";

    public EMMessage.Direct direct;
    public long time;
    public String msgText;
    public String from;
    public String to;
    public String showTime;

    private int hideTime;

    public MsgObject(EMMessage emMessage) {
        direct = emMessage.direct();
        time = emMessage.getMsgTime();
        msgText = ((EMTextMessageBody) emMessage.getBody()).getMessage();
        from = emMessage.getFrom();
        to = emMessage.getTo();
        showTime = NOT_SHOW;
        hideTime = 3;
    }

    public void setHideTime(int hideTime) {
        this.hideTime = hideTime;
    }

    /**
     * 获取默认格式显示的时间
     * 用于会话列表界面，显示的是每个会话的最后消息时间
     *
     * @return
     */
    public String getConversationLastTime() {
        long currentTime = System.currentTimeMillis();
        String msgTime = DateUtil.getTimeFromTime(time);

        int dayOffset = DateUtil.getDayOffset(time, currentTime);
        if (dayOffset < 1) {
            //和当前时间的间隔 <1天，显示时间
            return msgTime;
        } else if (dayOffset == 1) {
            //和当前时间的间隔 =1天，显示昨天
            return "昨天 ";
        } else if (dayOffset == 2) {
            //和当前时间的间隔 =2天，显示前天
            return "前天 ";
        } else if (dayOffset < 7) {
            //和当前时间的间隔 <7天，显示周几
            return DateUtil.getWeekDayFromTime(time);
        }
        int yearOffset = DateUtil.getYearOffset(time, currentTime);
        if (yearOffset < 1) {
            //和当前时间的间隔 <1年，显示日期
            return DateUtil.getDateFromTime(time);
        }
        //和当前时间的间隔 >=1年，显示具体日期
        return DateUtil.getYearDateFromTime(time);
    }

    /**
     * @return
     */
    public String getHistoryTime() {
        long currentTime = System.currentTimeMillis();
        String msgTime = DateUtil.getTimeFromTime(time);

        int dayOffset = DateUtil.getDayOffset(time, currentTime);
        if (dayOffset < 1) {
            //显示时间
            return msgTime;
        } else if (dayOffset == 1) {
            //显示昨天+时间
            return "昨天 " + msgTime;
        } else if (dayOffset == 2) {
            //显示前+时间
            return "前天 " + msgTime;
        } else if (dayOffset < 7) {
            //显示周几+时间
            return DateUtil.getWeekDayFromTime(time);
        }
        int yearOffset = DateUtil.getYearOffset(time, currentTime);
        if (yearOffset < 1) {
            //显示日期
            return DateUtil.getDateFromTime(time);
        }
        //显示具体日期
        return DateUtil.getYearDateFromTime(time);
    }

    public boolean moreThanHideTime(long preTime){
        int offset = DateUtil.getMinOffset(time, preTime);
        if (offset < hideTime) {
            return false;
        }
        return true;
    }
}
