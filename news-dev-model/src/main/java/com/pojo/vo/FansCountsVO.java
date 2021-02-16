package com.pojo.vo;

/**
 * 男女粉丝统计VO对象
 *
 * @author Json
 * @date 2021/2/14 16:37
 */
public class FansCountsVO {

    /**
     * 男粉丝数
     */
    private Integer manCounts;

    /**
     * 女粉丝数
     */
    private Integer womanCounts;

    public Integer getManCounts() {
        return manCounts;
    }

    public void setManCounts(Integer manCounts) {
        this.manCounts = manCounts;
    }

    public Integer getWomanCounts() {
        return womanCounts;
    }

    public void setWomanCounts(Integer womanCounts) {
        this.womanCounts = womanCounts;
    }
}
