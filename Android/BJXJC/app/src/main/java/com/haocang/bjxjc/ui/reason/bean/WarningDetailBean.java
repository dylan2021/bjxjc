package com.haocang.bjxjc.ui.reason.bean;

import com.haocang.bjxjc.ui.event.EventBean;
import com.haocang.bjxjc.ui.waterpoint.bean.WaterPointBean;
import com.haocang.bjxjc.utils.bean.EarlyWarningActionBean;

import java.util.List;

/**
 * 创建时间：2019/7/17
 * 编 写 人：ShenC
 * 功能描述：
 */
public class WarningDetailBean {

    private List<EarlyWarningActionBean> ReasonDetail;
    private List<WaterPointBean> WaterPoint;
    private List<EventBean> Event;
    private List<FloodUser> FloodUser;
    private List<FloodTeam> FloodTeam;
    private List<HisDataBean> HisData;

    public List<EarlyWarningActionBean> getReasonDetail() {
        return ReasonDetail;
    }

    public List<WaterPointBean> getWaterPoint() {
        return WaterPoint;
    }

    public List<EventBean> getEvent() {
        return Event;
    }

    public List<FloodUser> getFloodUser() {
        return FloodUser;
    }

    public List<FloodTeam> getFloodTeam() {
        return FloodTeam;
    }

    public List<HisDataBean> getHisData() {
        return HisData;
    }

    public class FloodUser {
        private String ID;
        private String UserID;
        private String UserName;
        private String Type;
        private String ArmyID;

        public String getID() {
            return ID;
        }

        public String getUserID() {
            return UserID;
        }

        public String getUserName() {
            return UserName;
        }

        public String getType() {
            return Type;
        }

        public String getArmyID() {
            return ArmyID;
        }
    }

    public class FloodTeam {
        private String ID;
        private String TeamName;
        private String LeaderName;
        private String Leader;
        private String LeaderTel;
        private String TeamMemberName;

        public String getID() {
            return ID;
        }

        public String getTeamName() {
            return TeamName;
        }

        public String getLeader() {
            return Leader;
        }

        public String getLeaderName() {
            return LeaderName;
        }

        public String getLeaderTel() {
            return LeaderTel;
        }

        public String getTeamMemberName() {
            return TeamMemberName;
        }
    }


}
